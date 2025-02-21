package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.application.video.delete.DeleteVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.VideoClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.ImageResourceDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.VideoResourceDto;
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
public class VideoListener {

    private static final Logger LOG = LoggerFactory.getLogger(VideoListener.class);

    private static final TypeReference<MessageValue<VideoEvent>> VIDEO_MESSAGE = new TypeReference<>() {
    };

    private final VideoClient videoClient;

    private final SaveVideoUseCase saveVideoUseCase;

    private final DeleteVideoUseCase deleteVideoUseCase;

    public VideoListener(
            final VideoClient videoClient,
            final SaveVideoUseCase saveVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase
    ) {
        this.videoClient = Objects.requireNonNull(videoClient);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.videos.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.videos.topics}",
            groupId = "${kafka.consumers.videos.group-id}",
            id = "${kafka.consumers.videos.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.videos.auto-offset-reset}"
            }
    )
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumers.videos.max-attempts}",
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

        final var messagePayload = Json.readValue(payload, VIDEO_MESSAGE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)) {
            deleteVideoUseCase.execute(new DeleteVideoUseCase.Input(messagePayload.before().id()));
        } else {
            videoClient.videoOfId(messagePayload.after().id())
                    .map(it -> new SaveVideoUseCase.Input(
                            it.id(),
                            it.title(),
                            it.description(),
                            it.yearLaunched(),
                            it.duration(),
                            it.rating(),
                            it.opened(),
                            it.published(),
                            it.createdAt(),
                            it.updatedAt(),
                            it.getVideo().map(VideoResourceDto::encodedLocation).orElse(""),
                            it.getTrailer().map(VideoResourceDto::encodedLocation).orElse(""),
                            it.getBanner().map(ImageResourceDto::location).orElse(""),
                            it.getThumbnail().map(ImageResourceDto::location).orElse(""),
                            it.getThumbnailHalf().map(ImageResourceDto::location).orElse(""),
                            it.categoriesId(),
                            it.castMembersId(),
                            it.genresId()
                    ))
                    .ifPresentOrElse(
                            saveVideoUseCase::execute,
                            () -> LOG.warn("video nao foi encontrado :: {}", messagePayload.after().id())
                    );
        }

    }

    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("mensagem recebida do kafka do DLT :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);
    }

}
