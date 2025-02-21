package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.application.genre.delete.DeleteGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GenreClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GenreListener {

    private static final Logger LOG = LoggerFactory.getLogger(GenreListener.class);

    private static final TypeReference<MessageValue<GenreEvent>> GENRE_MESSAGE = new TypeReference<>() {
    };

    private final GenreClient genreClient;

    private final SaveGenreUseCase saveGenreUseCase;

    private final DeleteGenreUseCase deleteGenreUseCase;

    public GenreListener(
            final GenreClient genreClient,
            final SaveGenreUseCase saveGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase
    ) {
        this.genreClient = Objects.requireNonNull(genreClient);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.genres.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.genres.topics}",
            groupId = "${kafka.consumers.genres.group-id}",
            id = "${kafka.consumers.genres.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.genres.auto-offset-reset}"
            }
    )
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumers.genres.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload(required = false) final String payload, final ConsumerRecordMetadata metadata) {
        if (payload == null) {
            LOG.info("mensagem vazia recebida do kafka :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                    metadata.partition(), metadata.topic(), metadata.offset(), payload);
            return;
        }
        LOG.info("mensagem recebida do kafka :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);

        final var messagePayload = Json.readValue(payload, GENRE_MESSAGE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)) {
            deleteGenreUseCase.execute(new DeleteGenreUseCase.Input(messagePayload.before().id()));
        } else {
            genreClient.genreOfId(messagePayload.after().id())
                    .map(it -> new SaveGenreUseCase.Input(
                            it.id(), it.name(), it.isActive(), it.categoriesId(), it.createdAt(), it.updatedAt(), it.deletedAt()
                    ))
                    .ifPresentOrElse(
                            saveGenreUseCase::execute,
                            () -> LOG.warn("genre nao foi encontrada :: {}", messagePayload.after().id())
                    );
        }

    }

    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("mensagem recebida do kafka do DLT :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);
    }

}
