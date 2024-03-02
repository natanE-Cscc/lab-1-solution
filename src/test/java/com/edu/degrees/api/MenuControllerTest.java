package com.edu.degrees.api;

import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.data.MenuItemRepository;
import com.edu.degrees.domain.MenuCategory;
import com.edu.degrees.domain.MenuItem;
import com.edu.degrees.domain.MenuOptions;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {
    @MockBean
    private MenuItemRepository mockItemRepository;

    @MockBean
    private DegreesBlogRepository mockRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String RESOURCE_URI = "/public/api/menus";
    private static final MenuCategory savedPosting = new MenuCategory(1L, "categoryTitle", "categoryNotes", 6);

@Test
@DisplayName("T01 - Get menus categories returns data ")
public void test_01(@Autowired MockMvc mockMvc) throws Exception {
    when(mockRepository.findAllByOrderBySortOrderAscCategoryTitleAsc()).thenReturn(Collections.singletonList(savedPosting));

    mockMvc.perform(get(RESOURCE_URI))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    verify(mockRepository, times(1)).findAllByOrderBySortOrderAscCategoryTitleAsc();
    verifyNoMoreInteractions(mockRepository);
}


}


