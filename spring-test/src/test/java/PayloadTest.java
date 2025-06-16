import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabycode.Main;
import com.javabycode.model.Fruit;
import com.javabycode.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class PayloadTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldRejectTooLargeInput() throws Exception {
        String jwt = jwtUtil.generateTestToken("admin", List.of("ROLE_ADMIN"));

        String largeName = "a".repeat(10000);
        Fruit fruit = new Fruit();
        fruit.setName(largeName);
        fruit.setProduceBy("Fazenda");

        mockMvc.perform(post("/fruits")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isBadRequest());
    }
}
