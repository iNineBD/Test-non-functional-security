import com.javabycode.Main;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class CORSFilterTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String PUBLIC_ENDPOINT = "/hello"; // Usar um endpoint público para testar CORS

    @Test
    void shouldAllowAllOriginsForSimpleGetRequest() throws Exception {
        mockMvc.perform(get(PUBLIC_ENDPOINT)
                        .header("Origin", "http://malicious-site.com")) // Simula uma requisição de outra origem
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "*")) // Verifica se o cabeçalho '*' está presente
                .andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"));
    }

    @Test
    void shouldAllowAllOriginsForPreflightRequest() throws Exception {
        // Simula uma requisição OPTIONS (preflight request) de uma origem diferente
        mockMvc.perform(options(PUBLIC_ENDPOINT)
                        .header("Origin", "http://another-domain.net")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Content-Type, Authorization"))
                .andExpect(status().isOk()) // Requisição preflight deve retornar 200 OK
                .andExpect(header().string("Access-Control-Allow-Origin", "*"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"))
                .andExpect(header().string("Access-Control-Allow-Headers",
                        "X-PINGOTHER,Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization"))
                .andExpect(header().string("Access-Control-Max-Age", "3600"));
    }

    @Test
    void shouldIncludeExposeHeaders() throws Exception {
        mockMvc.perform(get(PUBLIC_ENDPOINT)
                        .header("Origin", "http://test-domain.org"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Expose-Headers", "xsrf-token"));
    }

    // Discussão de cenário para produção:
    // Este teste valida que o CORS está configurado para '*' (qualquer origem).
    // Para um ambiente de produção, geralmente é mais seguro restringir o Access-Control-Allow-Origin
    // para domínios específicos que precisam acessar sua API.
    // Exemplo: .header("Access-Control-Allow-Origin", "https://seu-dominio-frontend.com")
    // Se o seu objetivo for permitir acesso de qualquer origem (por exemplo, uma API pública),
    // a configuração atual é aceitável, mas é crucial ter consciência do seu impacto na segurança.
}