import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabycode.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class AuthControllerBruteForceTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String LOGIN_URL = "/login";
    // IMPORTANTE: Este username deve ser um usuário válido (existente no seu banco de dados)
    // para que o AuthController.java consiga rastrear as tentativas falhas para ele.
    // Se você não tiver um usuário 'testuser' pré-cadastrado, ajuste para um existente ou adicione um @BeforeAll para registrá-lo.
    private static final String VALID_USERNAME = "admin"; // Ajuste se necessário para um usuário existente
    private static final String INVALID_PASSWORD = "wrongpassword"; // Senha que garantidamente falhará
    private static final int MAX_ATTEMPTS = 5; // Deve corresponder ao valor em AuthController.java

    // Objeto para serializar payloads para JSON
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Opcional: Um setup para garantir que o usuário 'testuser' existe.
    // Se seu banco de dados de teste for limpo a cada execução, você pode precisar registrar o usuário.


    @Test
    void shouldReturnTooManyRequestsAfterMaxAttempts() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("admin", VALID_USERNAME);
        loginRequest.put("wrongpassword", INVALID_PASSWORD); // Usar senha inválida para falhar

        // 1. Realizar MAX_ATTEMPTS - 1 tentativas falhas (ainda não bloqueia)
        for (int i = 0; i < MAX_ATTEMPTS - 1; i++) {
            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRequest)))
                    .andExpect(status().isUnauthorized()); // Espera 401 para tentativas falhas
        }

        // 2. Realizar a MAX_ATTEMPTS-ésima tentativa (esta deve ser a que ativa o bloqueio)
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isUnauthorized()); // Ainda 401, mas agora o contador atingiu o limite

        // 3. A próxima tentativa (MAX_ATTEMPTS + 1) deve resultar em 429 Too Many Requests
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().is(429)); // Espera 429 (Too Many Requests)
    }

    @Test
    void shouldReturnUnauthorizedBeforeMaxAttempts() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", VALID_USERNAME);
        loginRequest.put("password", INVALID_PASSWORD);

        // Realizar tentativas de login falhas, mas menos que o limite
        for (int i = 0; i < MAX_ATTEMPTS - 2; i++) { // Por exemplo, MAX_ATTEMPTS - 2 tentativas
            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRequest)))
                    .andExpect(status().isUnauthorized()); // Deve continuar retornando 401
        }
        // Uma última tentativa antes de atingir o limite
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isUnauthorized()); // Ainda 401
    }
}