package com.edu.degrees.api;

import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.domain.MenuCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DegreesBlogControllerTests {
    public static final String RESOURCE_URI = "/api/menu/categories";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MenuCategory testPosting = new MenuCategory(0L,"categoryTitle", "categoryNotes", 6 );
    private static final MenuCategory savedPosting = new MenuCategory(1L,"categoryTitle", "categoryNotes", 6 );
    @MockBean
    DegreesBlogRepository mockRepository;
    @Test
    @DisplayName("Post accepts and  returns degree post representation")
    public void postCreateNewDegreeEntry(@Autowired MockMvc mockMvc) throws Exception {
        when(mockRepository.save(any())).thenReturn(savedPosting);
        MockHttpServletRequestBuilder post = post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testPosting));
        MvcResult result = mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedPosting.getId()))
                .andExpect(jsonPath("$.categoryTitle").value(savedPosting.getCategoryTitle()))
                .andExpect(jsonPath("$.categoryNotes").value(savedPosting.getCategoryNotes()))
                .andExpect(jsonPath("$.sortOrder").value(savedPosting.getSortOrder()))
                .andReturn();
        assertEquals(
                String.format("http://localhost/api/menu/categories/%d", savedPosting.getId()),
                result.getResponse().getHeader("Location"));



    }
}
