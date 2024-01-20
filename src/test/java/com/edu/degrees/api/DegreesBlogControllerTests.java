package com.edu.degrees.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DegreesBlogControllerTests {
    @Test
    @DisplayName("Post returns status code CREATED")
    public void test01(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post("/api/menu/categories")).andExpect(status().isCreated());
    }
}
