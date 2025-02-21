package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.application.category.delete.DeleteCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.CategoryClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryEvent;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.json.Json;
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
public class CategoryListener {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListener.class);

    private static final TypeReference<MessageValue<CategoryEvent>> CATEGORY_MESSAGE = new TypeReference<>() {
    };

    private final CategoryClient categoryClient;

    private final SaveCategoryUseCase saveCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryListener(
            final CategoryClient categoryClient,
            final SaveCategoryUseCase saveCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase
    ) {
        this.categoryClient = Objects.requireNonNull(categoryClient);
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.categories.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.categories.topics}",
            groupId = "${kafka.consumers.categories.group-id}",
            id = "${kafka.consumers.categories.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.categories.auto-offset-reset}"
            }
    )
    // configurando as definicoes de retry, e retry sera enviado para uma outra fila
    // tornando o processamento nao bloqueante na fila principal
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumers.categories.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("mensagem recebida do kafka :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);

        final var messagePayload = Json.readValue(payload, CATEGORY_MESSAGE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)) {
            deleteCategoryUseCase.execute(messagePayload.before().id());
        } else {
            categoryClient.categoryOfId(messagePayload.after().id())
                    .ifPresentOrElse(
                            saveCategoryUseCase::execute,
                            () -> LOG.warn("categoria nao foi encontrada :: {}", messagePayload.after().id())
                    );
        }

    }

    // em caso de erro
    @DltHandler
    public void onDLTMessage(@Payload(required = false) final String payload, final ConsumerRecordMetadata metadata) {
        if (payload == null) {
            LOG.info("mensagem vazia recebida do kafka :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                    metadata.partition(), metadata.topic(), metadata.offset(), payload);
            return;
        }
        LOG.info("mensagem recebida do kafka do DLT :: partition :: {}, topic :: {}, offset :: {} :::: {}",
                metadata.partition(), metadata.topic(), metadata.offset(), payload);

        final var messagePayload = Json.readValue(payload, CATEGORY_MESSAGE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)) {
            deleteCategoryUseCase.execute(messagePayload.before().id());
        } else {
            categoryClient.categoryOfId(messagePayload.after().id())
                    .ifPresentOrElse(
                            saveCategoryUseCase::execute,
                            () -> LOG.warn("categoria nao foi encontrada :: {}", messagePayload.after().id())
                    );
        }
    }

}
