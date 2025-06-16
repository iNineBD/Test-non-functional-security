import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.javabycode.Main.class)
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEndpointShouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());
    }

    @Test
    void privateEndpointShouldRequireAuth() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void privateEndpointShouldBeAccessibleWithCorrectCredentials() throws Exception {
        mockMvc.perform(get("/admin").with(httpBasic("javabycode", "123456")))
                .andExpect(status().isOk());
    }

    @Test
    void privateEndpointShouldRejectInvalidCredentials() throws Exception {
        mockMvc.perform(get("/admin").with(httpBasic("wrong", "wrong")))
                .andExpect(status().isUnauthorized());
    }
}
