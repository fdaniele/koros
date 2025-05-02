import it.koros.model.Message;
import it.koros.repository.MessageRepository;
import it.koros.service.KafkaMessageListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class KafkaMessageListenerTest {

    @Test
    void testListenShouldSaveMessageToMongo() {
        // Arrange
        MessageRepository messageRepository = mock(MessageRepository.class);
        KafkaMessageListener listener = new KafkaMessageListener(messageRepository);

        String payload = "{\"event\":\"TEST\",\"data\":\"value\"}";
        long now = System.currentTimeMillis();


        ConsumerRecord<String, String> record = new ConsumerRecord<>(
                "service-exchange-messages-out", 0, 0L, null, payload);
        record.headers().add(new RecordHeader("x-sent-producer-timestamp", String.valueOf(now - 100).getBytes()));
        record.headers().add(new RecordHeader("x-middleware-timestamp", String.valueOf(now - 50).getBytes()));

        // Act
        listener.listen(record);

        // Assert
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());

        Message saved = captor.getValue();
        assertEquals(payload, saved.getContent());
    }
}