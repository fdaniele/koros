import it.koros.producer.controller.KafkaProducerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = KafkaProducerController.class)
@ContextConfiguration(classes = {KafkaProducerController.class})
class KafkaProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void testSendSecureMessageReturnsOk() throws Exception {
        // Setup del mock
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/api/secure/send")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("payload", "test-message"))
                .andExpect(status().isOk())
                .andExpect(content().string("Messaggio sicuro inviato a Kafka con headers di tracing e security"));

    }
}
