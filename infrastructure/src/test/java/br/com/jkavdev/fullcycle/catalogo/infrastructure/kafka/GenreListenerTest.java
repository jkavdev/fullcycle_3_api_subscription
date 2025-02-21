package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import br.com.jkavdev.fullcycle.catalogo.application.genre.delete.DeleteGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GenreClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.ValuePayload;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class GenreListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private SaveGenreUseCase saveGenreUseCase;

    @MockBean
    private GenreClient genreClient;

    @SpyBean
    private GenreListener genreListener;

    @Value("${kafka.consumers.genres.topics}")
    private String genreTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadataCaptor;

    @Test
    public void testGenresTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.genres";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.genres-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.genres-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.genres-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.genres-dlt";

        // when
        final var actualTopics = admin().listTopics().listings().get(10, TimeUnit.SECONDS).stream()
                .map(TopicListing::name)
                .toList();
        Assertions.assertTrue(actualTopics.containsAll(List.of(
                expectedMainTopic,
                expectedRetry0Topic,
                expectedRetry1Topic,
                expectedRetry2Topic,
                expectedDLTTopic
        )));
    }

    @Test
    public void givenInvalidResponsesFromHandlerShouldRetryUntilGoesToDLT() throws Exception {
        // given
        final var expectedMaxAttemps = 4;
        final var expectedMaxDLTAttemps = 1;
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.genres";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.genres-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.genres-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.genres-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.genres-dlt";

        final var business = Fixture.Genres.business();
        final var businessEvent = new br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreEvent(business.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(businessEvent, businessEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    throw new RuntimeException("DEU RUIMMMMMMMMMMMMM");
                })
                .when(deleteGenreUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return null;
                })
                .when(genreListener)
                .onDLTMessage(ArgumentMatchers.any(), ArgumentMatchers.any());


        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(genreListener, Mockito.times(expectedMaxAttemps))
                .onMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        final var allMetas = metadataCaptor.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        Mockito.verify(genreListener, Mockito.times(expectedMaxDLTAttemps))
                .onDLTMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        Assertions.assertEquals(expectedDLTTopic, metadataCaptor.getValue().topic());
    }

    @Test
    public void givenUpdateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var business = Fixture.Genres.business();
        final var businessEvent = new GenreEvent(business.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(businessEvent, businessEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return new SaveGenreUseCase.Output(business.id());
                })
                .when(saveGenreUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doReturn(Optional.of(GenreDto.from(business)))
                .when(genreClient)
                .genreOfId(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(genreClient, Mockito.times(1)).genreOfId(business.id());

        Mockito.verify(saveGenreUseCase, Mockito.times(1)).execute(ArgumentMatchers.refEq(
                new SaveGenreUseCase.Input(
                        business.id(),
                        business.name(),
                        business.active(),
                        business.categories(),
                        business.createdAt(),
                        business.updatedAt(),
                        business.deletedAt()
                )
        ));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var business = Fixture.Genres.business();
        final var businessEvent = new GenreEvent(business.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(businessEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return new SaveGenreUseCase.Output(business.id());
                })
                .when(saveGenreUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doReturn(Optional.of(GenreDto.from(business)))
                .when(genreClient)
                .genreOfId(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(genreClient, Mockito.times(1)).genreOfId(business.id());

        Mockito.verify(saveGenreUseCase, Mockito.times(1)).execute(ArgumentMatchers.refEq(
                new SaveGenreUseCase.Input(
                        business.id(),
                        business.name(),
                        business.active(),
                        business.categories(),
                        business.createdAt(),
                        business.updatedAt(),
                        business.deletedAt()
                )
        ));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var business = Fixture.Genres.business();
        final var businessEvent = new br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreEvent(business.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, businessEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return null;
                })
                .when(deleteGenreUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doReturn(Optional.of(business))
                .when(genreClient)
                .genreOfId(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(genreTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(deleteGenreUseCase, Mockito.times(1)).execute(
                new DeleteGenreUseCase.Input(business.id())
        );
    }

}