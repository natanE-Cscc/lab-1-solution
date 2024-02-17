package com.edu.degrees.api;

import com.edu.degrees.data.MenuItemRepository;
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
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuItemControllerTest {
    public static final String RESOURCE_URI = "/api/menu/items";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MenuItem testPosting = new MenuItem(0L,null, "title", "this is a menu item", "$1", 4);
   private static final MenuItem savedPosting = new MenuItem(1L,null,"title", "this is a menu item", "$1", 6);
    @MockBean
    private MenuItemRepository mockItemRepository;

    @Test
    @DisplayName("Post accepts and returns category post representation")
    public void postCreatesNewItemEntry(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.save(refEq(testPosting, "menuCategory"))).thenReturn(savedPosting);
        MockHttpServletRequestBuilder post = post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testPosting));
        MvcResult result = mockMvc.perform(post)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedPosting.getId()))
                .andExpect(jsonPath("$.menuCategory").value(savedPosting.getMenuCategory()))
                .andExpect(jsonPath("$.name").value(savedPosting.getName()))
                .andExpect(jsonPath("$.description").value(savedPosting.getDescription()))
                .andExpect(jsonPath("$.sortOrder").value(savedPosting.getSortOrder().toString()))
                .andReturn();

        assertEquals(
                String.format("http://localhost/api/menu/items/%d", savedPosting.getId()),
                result.getResponse().getHeader("Location"));
        verify(mockItemRepository, times(1)).save(refEq(testPosting, "menuCategory"));
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T02 - When no items exist, GET returns an empty list")
    public void test_02(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.findAll()).thenReturn(new ArrayList());
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mockItemRepository, times(1)).findAll();
        verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    @DisplayName("T03 - When one item exists, GET returns a list with it")
    public void test_03(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.findAll()).thenReturn(Collections.singletonList(savedPosting));
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(savedPosting.getId()))
                .andExpect(jsonPath("$.[0].menuCategory").value(savedPosting.getMenuCategory()))
                .andExpect(jsonPath("$.[0].name").value(savedPosting.getName()))
                .andExpect(jsonPath("$.[0].description").value(savedPosting.getDescription()))
                .andExpect(jsonPath("$.[0].price").value(savedPosting.getPrice()))
                .andExpect(jsonPath("$.[0].sortOrder").value(savedPosting.getSortOrder().toString()))
                .andExpect(status().isOk());
        verify(mockItemRepository, times(1)).findAll();
        verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    @DisplayName("T04 - Requested Item does not exist So GET returns 404")
    public void test_04(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(mockItemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T05 - Requested Items exists so GET returns it in a list with Id")
    public void test_05(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedPosting));
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(savedPosting.getId()))
                .andExpect(jsonPath("$.[0].menuCategory").value(savedPosting.getMenuCategory()))
                .andExpect(jsonPath("$.[0].name").value(savedPosting.getName()))
                .andExpect(jsonPath("$.[0].description").value(savedPosting.getDescription()))
                .andExpect(jsonPath("$.[0].price").value(savedPosting.getPrice()))
                .andExpect(jsonPath("$.[0].sortOrder").value(savedPosting.getSortOrder().toString()))
                .andExpect(status().isOk());
        verify(mockItemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T06 - Article to be updated does not exist so PUT returns 404")
    public void test_06(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.existsById(10L)).thenReturn(false);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new MenuItem(10L, null,  "titleOne", "this is a put menu test item","$2",8))))
                .andExpect(status().isNotFound());
        verify(mockItemRepository, never()).save(any(MenuItem.class));
        verify(mockItemRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T07 - Item to be updated exists so PUT saves new copy")
    public void test_07(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.existsById(10L)).thenReturn(true);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new MenuItem(10L, null,  "titleOne", "this is a put menu test item","$2",8))))
                .andExpect(status().isNoContent());
        verify(mockItemRepository, times(1)).save(any(MenuItem.class));
        verify(mockItemRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T08 - ID in PUT URL not equal to one in request body")
    public void test_08(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(RESOURCE_URI + "/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new MenuItem(10L, null,  "titleOne", "this is a put menu test item","$2",8))))
                .andExpect(status().isConflict());
        verify(mockItemRepository, never()).save(any(MenuItem.class));
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T09 - Items to be removed does not exist so DELETE returns 404")
    public void test_09(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.findById(1L))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(mockItemRepository, never()).delete(any(MenuItem.class));
        verify(mockItemRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T10 - Items to be removed exists so DELETE deletes it")
    public void test_10(@Autowired MockMvc mockMvc) throws Exception {
        when(mockItemRepository.findById(1L))
                .thenReturn(Optional.of(savedPosting));
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNoContent());
        verify(mockItemRepository, times(1)).delete(refEq(savedPosting));
        verify(mockItemRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(mockItemRepository);
    }
    @Test
    @DisplayName("T11 - POST returns 400 if required properties are not set")
    public void testItem_11(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MenuItem())))
                .andExpect(status().isBadRequest());
        verify(mockItemRepository, never()).save(any(MenuItem.class));
        verifyNoMoreInteractions(mockItemRepository);
    }

    @Test
    @DisplayName("T12 - Field errors present for each invalid property")
    public void test_122(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MenuItem())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.menuCategory").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.price").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.sortOrder").value("must not be null"));
        mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new MenuItem(0L, null,  " ", " "," ",8))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.menuCategory").value(
                        "menuCategory is required"))
                .andExpect(jsonPath("$.fieldErrors.name").value(
                "Please enter a name of up to 80 characters"))
                .andExpect(jsonPath("$.fieldErrors.price")
                        .value("Please enter a price up to 20 characters"))
                .andExpect(jsonPath("$.fieldErrors.sortOrder")
                        .value("sortOrder is required"));
        verify(mockItemRepository, never()).save(any(MenuItem.class));
    }

}


