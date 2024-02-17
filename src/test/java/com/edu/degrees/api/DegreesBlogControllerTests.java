package com.edu.degrees.api;

import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.domain.MenuCategory;
import com.edu.degrees.domain.MenuItem;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DegreesBlogControllerTests {
    public static final String RESOURCE_URI = "/api/menu/categories";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MenuCategory testPosting = new MenuCategory(0L,"categoryTitle", "categoryNotes", 6 );
    private static final MenuCategory savedPosting = new MenuCategory(1L,"categoryTitle", "categoryNotes", 6 );
   // private static final MenuCategory savedValidPosting = new MenuCategory(0L, null, "categoryValidNotes ", 12);
    @MockBean
    private DegreesBlogRepository degreesBlogRepository;
    @Test
    @DisplayName("Post accepts and  returns degree post representation")
    public void testCategory_01(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.save(any())).thenReturn(savedPosting);
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
    @Test
    @DisplayName("T02 - When no Category exist, GET returns an empty list")
    public void testCategory_02(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.findAll()).thenReturn(new ArrayList());
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(degreesBlogRepository, times(1)).findAll();
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T03 - When one category exists, GET returns a list with it")
    public void testCategory_03(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.findAll()).thenReturn(Collections.singletonList(savedPosting));
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(savedPosting.getId()))
                .andExpect(jsonPath("$.[0].categoryTitle").value(savedPosting.getCategoryTitle()))
                .andExpect(jsonPath("$.[0].categoryNotes").value(savedPosting.getCategoryNotes()))
                .andExpect(jsonPath("$.[0].sortOrder").value(savedPosting.getSortOrder().toString()))
                .andExpect(status().isOk());
        verify(degreesBlogRepository, times(1)).findAll();
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T04 - Requested category does not exist So GET returns 404")
    public void testCategory_04(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(degreesBlogRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T05 - Requested Items exists so GET returns it in a list by Id")
    public void testCategory_05(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedPosting));
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(savedPosting.getId()))
                .andExpect(jsonPath("$.[0].categoryTitle").value(savedPosting.getCategoryTitle()))
                .andExpect(jsonPath("$.[0].categoryNotes").value(savedPosting.getCategoryNotes()))
                .andExpect(jsonPath("$.[0].sortOrder").value(savedPosting.getSortOrder().toString()))
                .andExpect(status().isOk());
        verify(degreesBlogRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T06 - Category to be updated does not exist so PUT returns 404")
    public void testCategory_06(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.existsById(10L)).thenReturn(false);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new MenuCategory(10L,  "categoryTitleTest", "categoryNotesTest",8))))
                .andExpect(status().isNotFound());
        verify(degreesBlogRepository, never()).save(any(MenuCategory.class));
        verify(degreesBlogRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T07 - category to be updated exists so PUT saves new copy")
    public void testCategory_07(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.existsById(10L)).thenReturn(true);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new MenuCategory(10L,  "categoryTitleTest", "categoryNotesTest",8))))
                .andExpect(status().isNoContent());
        verify(degreesBlogRepository, times(1)).save(any(MenuCategory.class));
        verify(degreesBlogRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T08 - ID in PUT URL not equal to one in request body")
    public void testCategory_08(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(RESOURCE_URI + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new MenuCategory(10L,  "categoryTitleTest", "categoryNotesTest",8))))
                .andExpect(status().isConflict());
        verify(degreesBlogRepository, never()).save(any(MenuCategory.class));
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T09 - Category to be removed does not exist so DELETE returns 404")
    public void testCategory_09(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.findById(1L))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(degreesBlogRepository, never()).delete(any(MenuCategory.class));
        verify(degreesBlogRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T10 - Category to be removed exists so DELETE deletes it")
    public void testCategory_10(@Autowired MockMvc mockMvc) throws Exception {
        when(degreesBlogRepository.findById(1L))
                .thenReturn(Optional.of(savedPosting));
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNoContent());
        verify(degreesBlogRepository, times(1)).delete(refEq(savedPosting));
        verify(degreesBlogRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(degreesBlogRepository);
    }
    @Test
    @DisplayName("T11 - POST returns 400 if required properties are not set")
    public void testCategory_11(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MenuCategory())))
                .andExpect(status().isBadRequest());
        verify(degreesBlogRepository, never()).save(any(MenuCategory.class));
        verifyNoMoreInteractions(degreesBlogRepository);
    }

    @Test
    @DisplayName("T12 - Field errors present for each invalid property")
    public void test_122(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MenuCategory())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.categoryTitle").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.sortOrder").value("must not be null"));
        mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MenuCategory(0L, null, "", 6))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.categoryTitle").value(
                        "Please enter a category name of up to 200 characters"))
                //.andExpect(jsonPath("$.fieldErrors.title").value(
                        //"Please enter a title up to 200 characters in length"))
                .andExpect(jsonPath("$.fieldErrors.sortOrder")
                        .value("Content is required"));
        verify(degreesBlogRepository, never()).save(any(MenuCategory.class));
    }



}
