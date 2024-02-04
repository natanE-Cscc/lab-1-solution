package com.edu.degrees.api;

import com.edu.degrees.data.MenuItemCrudRepository;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuItemControllerTest {

    @MockBean
    private MenuItemCrudRepository mockItemRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final MenuItem testPosting = new MenuItem(0L, new MenuCategory(0L, "category", "title", 6), "title", "this is a menu item", "$1", 4);


    private static final String RESOURCE_URI = "/api/menu/items";

    @Test
    @DisplayName("T01 - POST accepts and returns blog post representation")
    public void postCreatesMenuItemEntry_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        when(mockItemRepository.save(any(MenuItem.class))).thenReturn(testPosting);
        //when(mockRepository.save(any(MenuCategory.class))).thenReturn(savedPosting);
        MvcResult result = mockMvc.perform(post(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testPosting)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "$.id").value(testPosting.getId()))
                .andExpect(jsonPath(
                        "$.name").value(testPosting.getName()))
                .andExpect(
                        jsonPath("$.description").value(testPosting.getDescription()))
                .andExpect(jsonPath(
                        "$.price").value(testPosting.getPrice()))
                .andExpect(jsonPath(
                        "$.sortOrder").value(testPosting.getSortOrder()))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        assertEquals(
                "http://localhost/api/menu/items",
                mockResponse.getHeader("Location"));
        verify(mockItemRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("T02 - GET Returns menu item")
    public void getGetsMenuItemEntry_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        List <MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(testPosting);
        when(mockItemRepository.findAll()).thenReturn(menuItemList);
        //when(mockRepository.save(any(MenuCategory.class))).thenReturn(savedPosting);
        MvcResult result = mockMvc.perform(get(RESOURCE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testPosting)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "[0].id").value(testPosting.getId()))
                .andExpect(jsonPath(
                        "[0].name").value(testPosting.getName()))
                .andExpect(
                        jsonPath("[0].description").value(testPosting.getDescription()))
                .andExpect(jsonPath(
                        "[0].price").value(testPosting.getPrice()))
                .andExpect(jsonPath(
                        "[0].sortOrder").value(testPosting.getSortOrder()))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        verify(mockItemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("T01 - GET Returns menu item ID")
    public void getGetsMenuItemEntryId_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        when(mockItemRepository.findById(refEq(testPosting.getId()))).thenReturn(java.util.Optional.of(testPosting));
        //when(mockRepository.save(any(MenuCategory.class))).thenReturn(savedPosting);
        MvcResult result = mockMvc.perform(get(RESOURCE_URI + "/" + testPosting.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testPosting)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.id").value(testPosting.getId()))
                .andExpect(jsonPath(
                        "$.name").value(testPosting.getName()))
                .andExpect(
                        jsonPath("$.description").value(testPosting.getDescription()))
                .andExpect(jsonPath(
                        "$.price").value(testPosting.getPrice()))
                .andExpect(jsonPath(
                        "$.sortOrder").value(testPosting.getSortOrder()))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        verify(mockItemRepository, times(1)).findById(testPosting.getId());
    }

    @Test
    @DisplayName("T01 - Deletes menu item ID")
    public void getDeletesMenuItemEntryId_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        when(mockItemRepository.findById(refEq(testPosting.getId()))).thenReturn(java.util.Optional.of(testPosting));
        //when(mockRepository.save(any(MenuCategory.class))).thenReturn(savedPosting);
        MvcResult result = mockMvc.perform(delete(RESOURCE_URI + "/" + testPosting.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testPosting)))
                .andExpect(status().isNoContent())
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        verify(mockItemRepository, times(1)).findById(testPosting.getId());
    }

    @Test
    @DisplayName("T01 - PUT menu item ID")
    public void getPutsMenuItemEntry_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        when(mockItemRepository.findById(refEq(testPosting.getId()))).thenReturn(java.util.Optional.of(testPosting));
        //when(mockRepository.save(any(MenuCategory.class))).thenReturn(savedPosting);
        MvcResult result = mockMvc.perform(put(RESOURCE_URI + "/" + testPosting.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testPosting)))
                .andExpect(status().isNoContent())
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        verify(mockItemRepository, times(1)).findById(testPosting.getId());
    }



}
