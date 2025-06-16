package com.javabycode.controller; // Make sure this is in same package as main app

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabycode.model.Fruit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class FruitControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllFruits_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/fruits"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllFruits_AsNonAdmin_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/fruits"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllFruits_AsAdmin_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/fruits"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createFruit_AsNonAdmin_ShouldReturnForbidden() throws Exception {
        Fruit fruit = new Fruit(1, "Apple", "Seu Armando");

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(fruit)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createFruit_AsAdmin_ShouldReturnCreated() throws Exception {
        Fruit fruit = new Fruit(1, "Apple", "Seu Armando");

        mockMvc.perform(post("/fruits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(fruit)))
                .andExpect(status().isCreated());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}