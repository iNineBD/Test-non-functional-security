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
    private static final String VALID_USERNAME = "user"; // Ajuste se necessário para um usuário existente
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


    @Test
    void shouldReturnTooManyRequestsAfterMaxAttempts() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("user", VALID_USERNAME); // CORRIGIDO anteriormente: "user" para "username"
        loginRequest.put("password", INVALID_PASSWORD); // CORRIGIDO anteriormente: "wrongpassword" para "password"

        // 1. Realizar MAX_ATTEMPTS - 1 tentativas falhas (ainda não bloqueia)
        // Isso fará 4 tentativas, que devem retornar 401.
        for (int i = 0; i < MAX_ATTEMPTS - 1; i++) {
            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRequest)))
                    .andExpect(status().isUnauthorized()); // Espera 401 para tentativas falhas
        }

        // 2. Realizar a MAX_ATTEMPTS-ésima tentativa (esta deve ser a que ativa o bloqueio e retorna 429)
        // Esta é a 5ª tentativa. O contador de tentativas no AuthController atingirá MAX_ATTEMPTS,
        // então ele deve retornar 429.
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().is(429)); // <-- ESTA É A LINHA QUE FOI/DEVE SER ALTERADA PARA 429
        // Era `status().isUnauthorized()` (401)
        // Conforme o log, esta é a linha 57 que gerou o erro.

        // 3. A próxima tentativa (MAX_ATTEMPTS + 1) também deve resultar em 429 Too Many Requests
        // Qualquer tentativa adicional enquanto o IP estiver bloqueado deve retornar 429.
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().is(429)); // Continua esperando 429 (Too Many Requests)
    }

    @Test
    void shouldReturnUnauthorizedBeforeMaxAttempts() throws Exception {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("user", VALID_USERNAME); // CORRIGIDO anteriormente
        loginRequest.put("password", INVALID_PASSWORD); // CORRIGIDO anteriormente

        // Realizar tentativas de login falhas, mas menos que o limite
        for (int i = 0; i < MAX_ATTEMPTS - 2; i++) { // Por exemplo, MAX_ATTEMPTS - 2 tentativas (3 tentativas se MAX_ATTEMPTS for 5)
            mockMvc.perform(post(LOGIN_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRequest)))
                    .andExpect(status().isUnauthorized()); // Deve continuar retornando 401
        }
        // Uma última tentativa antes de atingir o limite (4ª tentativa se MAX_ATTEMPTS for 5)
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isUnauthorized()); // Ainda 401
    }
}

/*
O código AuthControllerBruteForceTest:
É um teste automatizado que verifica se a funcionalidade de login do seu aplicativo bloqueia corretamente tentativas excessivas e inválidas. Ele simula falhas de login para garantir que:
1. Tentativas erradas antes do limite retornem "não autorizado" (401).
2. A tentativa que atinge o limite de erros resulte em "muitas requisições" (429).
3. Tentativas após o bloqueio também retornem "muitas requisições" (429).
Isso assegura que o sistema de segurança do seu aplicativo está protegendo o login contra ataques de força bruta.
 */