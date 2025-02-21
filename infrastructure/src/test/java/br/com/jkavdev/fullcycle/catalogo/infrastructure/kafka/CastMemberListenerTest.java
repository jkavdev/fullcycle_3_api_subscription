package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.CastMemberEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
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

class CastMemberListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @SpyBean
    private CastMemberListener castMemberListener;

    @Value("${kafka.consumers.cast-members.topics}")
    private String castmemberTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadataCaptor;

    @Test
    public void testCastMembersTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.cast_members";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.cast_members-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.cast_members-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.cast_members-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.cast_members-dlt";

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
        final var expectedMaxAttempts = 4;
        final var expectedMaxDLTAttempts = 1;
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.cast_members";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.cast_members-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.cast_members-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.cast_members-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.cast_members-dlt";

        final var expectedMember = Fixture.CastMembers.gabriel();
        final var expectedEvent = CastMemberEvent.from(expectedMember);

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, expectedEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    throw new RuntimeException("FUDEUUUUUUUUUUUUUUUUU!");
                })
                .when(deleteCastMemberUseCase)
                .execute(ArgumentMatchers.any());

        Mockito.doAnswer(t -> {
                            latch.countDown();
                            return null;
                        }
                )
                .when(castMemberListener)
                .onDLTMessage(Mockito.anyString(), Mockito.any());

        // when
        producer().send(new ProducerRecord<>(castmemberTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(castMemberListener, Mockito.times(expectedMaxAttempts))
                .onMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        final var allMetas = metadataCaptor.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        Mockito.verify(castMemberListener, Mockito.times(expectedMaxDLTAttempts))
                .onDLTMessage(ArgumentMatchers.eq(message), metadataCaptor.capture());

        Assertions.assertEquals(expectedDLTTopic, metadataCaptor.getValue().topic());
    }

    @Test
    public void givenUpdateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var expectedMember = Fixture.CastMembers.gabriel();
        final var expectedEvent = CastMemberEvent.from(expectedMember);

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(expectedEvent, expectedEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return expectedMember;
                })
                .when(saveCastMemberUseCase)
                .execute(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(castmemberTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(saveCastMemberUseCase, Mockito.times(1))
                .execute(Mockito.refEq(expectedMember, "createdAt", "updatedAt"));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var expectedMember = Fixture.CastMembers.gabriel();
        final var expectedEvent = CastMemberEvent.from(expectedMember);

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(expectedEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return expectedMember;
                })
                .when(saveCastMemberUseCase)
                .execute(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(castmemberTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(saveCastMemberUseCase, Mockito.times(1))
                .execute(Mockito.refEq(expectedMember, "createdAt", "updatedAt"));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var expectedMember = Fixture.CastMembers.gabriel();
        final var expectedEvent = new CategoryEvent(expectedMember.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, expectedEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        Mockito.doAnswer(t -> {
                    latch.countDown();
                    return null;
                })
                .when(deleteCastMemberUseCase)
                .execute(ArgumentMatchers.any());

        // when
        producer().send(new ProducerRecord<>(castmemberTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        Mockito.verify(deleteCastMemberUseCase, Mockito.times(1)).execute(expectedMember.id());
    }

}