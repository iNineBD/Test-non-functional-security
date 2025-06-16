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
public class InputTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void shouldAcceptValidInput() throws Exception {
        String jwt = jwtUtil.generateTestToken("user", List.of("ROLE_USER"));

        Fruit fruit = new Fruit();
        fruit.setName("Banana");
        fruit.setProduceBy("Fazenda");

        mockMvc.perform(post("/fruits")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRejectNullNameInput() throws Exception {
        String jwt = jwtUtil.generateTestToken("user", List.of("ROLE_USER"));

        Fruit fruit = new Fruit();
        fruit.setName("");
        fruit.setProduceBy("Fazenda");

        mockMvc.perform(post("/fruits")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNullProduceByInput() throws Exception {
        String jwt = jwtUtil.generateTestToken("user", List.of("ROLE_USER"));

        Fruit fruit = new Fruit();
        fruit.setName("Morango");
        fruit.setProduceBy("");

        mockMvc.perform(post("/fruits")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAllowAdminToCreateFruit() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Mango");
        fruit.setProduceBy("Farm");

        mockMvc.perform(post("/fruits")
                        .header("Authorization", "Bearer " + jwtUtil.generateTestToken("admin", List.of("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldAllowUserToCreateFruit() throws Exception {
        Fruit fruit = new Fruit();
        fruit.setName("Orange");
        fruit.setProduceBy("Farm");

        mockMvc.perform(post("/fruits")
                        .header("Authorization", "Bearer " + jwtUtil.generateTestToken("user", List.of("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit)))
                .andExpect(status().isCreated());
    }
}
