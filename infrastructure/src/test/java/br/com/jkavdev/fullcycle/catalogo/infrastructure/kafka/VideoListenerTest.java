package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import br.com.jkavdev.fullcycle.catalogo.application.video.delete.DeleteVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.ValuePayload;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.VideoClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.ImageResourceDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoResourceDto;
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

class VideoListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;

    @MockBean
    private VideoClient videoClient;

    @SpyBean
    private VideoListener videoListener;

    @Value("${kafka.consumers.videos.topics}")
    private String videoTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadataCaptor;

    @Test
    public void testVideosTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.videos";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.videos-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.videos-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.videos-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.videos-dlt";

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
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.videos";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.videos-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.videos-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.videos-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.videos-dlt";

        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(videoEvent, videoEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    throw new RuntimeException("DEU RUIMMMMMMMMMMMMM");
                })
                .when(deleteVideoUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return null;
                })
                .when(videoListener)
                .onDLTMessage(ArgumentMatchers.any(), ArgumentMatchers.any());


        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(videoListener, Mockito.times(expectedMaxAttemps))
                .onMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        final var allMetas = metadataCaptor.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        Mockito.verify(videoListener, Mockito.times(expectedMaxDLTAttemps))
                .onDLTMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        Assertions.assertEquals(expectedDLTTopic, metadataCaptor.getValue().topic());
    }

    @Test
    public void givenValidVideoUpdateOperationWhenProcessGoesOKThenShouldEndTheOperation() throws Exception {
        // given
        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(videoEvent, videoEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return new SaveVideoUseCase.Output(java21.id());
                })
                .when(saveVideoUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doReturn(Optional.of(videoDto(java21)))
                .when(videoClient)
                .videoOfId(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(videoClient, Mockito.times(1)).videoOfId(java21.id());

        Mockito.verify(saveVideoUseCase, Mockito.times(1)).execute(ArgumentMatchers.refEq(
                new SaveVideoUseCase.Input(
                        java21.id(),
                        java21.title(),
                        java21.description(),
                        java21.launchedAt().getValue(),
                        java21.duration(),
                        java21.rating().getName(),
                        java21.opened(),
                        java21.published(),
                        java21.createdAt().toString(),
                        java21.updatedAt().toString(),
                        java21.video(),
                        java21.trailer(),
                        java21.banner(),
                        java21.thumbnail(),
                        java21.thumbnailHalf(),
                        java21.categories(),
                        java21.castMembers(),
                        java21.genres()
                )
        ));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(videoEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return new SaveVideoUseCase.Output(java21.id());
                })
                .when(saveVideoUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doReturn(Optional.of(videoDto(java21)))
                .when(videoClient)
                .videoOfId(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(videoClient, Mockito.times(1)).videoOfId(java21.id());

        Mockito.verify(saveVideoUseCase, Mockito.times(1)).execute(ArgumentMatchers.refEq(
                new SaveVideoUseCase.Input(
                        java21.id(),
                        java21.title(),
                        java21.description(),
                        java21.launchedAt().getValue(),
                        java21.duration(),
                        java21.rating().getName(),
                        java21.opened(),
                        java21.published(),
                        java21.createdAt().toString(),
                        java21.updatedAt().toString(),
                        java21.video(),
                        java21.trailer(),
                        java21.banner(),
                        java21.thumbnail(),
                        java21.thumbnailHalf(),
                        java21.categories(),
                        java21.castMembers(),
                        java21.genres()
                )
        ));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var java21 = Fixture.Videos.java21();
        final var videoEvent = new VideoEvent(java21.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, videoEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return null;
                })
                .when(deleteVideoUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doReturn(Optional.of(java21))
                .when(videoClient)
                .videoOfId(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(videoTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(deleteVideoUseCase, Mockito.times(1)).execute(
                new DeleteVideoUseCase.Input(java21.id())
        );
    }

    private static VideoDto videoDto(final Video video) {
        return new VideoDto(
                video.id(),
                video.title(),
                video.description(),
                video.launchedAt().getValue(),
                video.rating().getName(),
                video.duration(),
                video.opened(),
                video.published(),
                imageResource(video.banner()),
                imageResource(video.thumbnail()),
                imageResource(video.thumbnailHalf()),
                videoResource(video.trailer()),
                videoResource(video.video()),
                video.categories(),
                video.castMembers(),
                video.genres(),
                video.createdAt().toString(),
                video.updatedAt().toString()
        );
    }

    private static VideoResourceDto videoResource(final String data) {
        return new VideoResourceDto(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data, data, "processed");
    }

    private static ImageResourceDto imageResource(final String data) {
        return new ImageResourceDto(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data);
    }

}