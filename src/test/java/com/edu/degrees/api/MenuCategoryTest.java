package com.edu.degrees.api;

import com.edu.degrees.domain.MenuCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MenuCategoryTest {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String RESOURCE_URI = "http://localhost:%d/api/menu/categories";


    private static final MenuCategory testPosting = new MenuCategory(0L, "categoryTitle", "categoryNotes", 6);


    @Test
    @DisplayName("T01 - POSTed location includes server port when created")
    public void test_01() {
        ResponseEntity<MenuCategory> responseEntity =
                this.restTemplate.postForEntity(String.format(RESOURCE_URI,
                        localServerPort), testPosting, MenuCategory.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(localServerPort,
                responseEntity.getHeaders().getLocation().getPort());


    }
    @Test
    @DisplayName("T02 - POST generates nonzero ID")
    public void test_02() {
        ResponseEntity<MenuCategory> responseEntity =
                this.restTemplate.postForEntity(String.format(RESOURCE_URI,localServerPort),
                        testPosting,MenuCategory.class);
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        MenuCategory blogPostReturned=responseEntity.getBody();
        assertNotEquals(testPosting.getId(),blogPostReturned.getId());
        assertEquals(String.format(RESOURCE_URI+"/%d",localServerPort,
                blogPostReturned.getId()),responseEntity.getHeaders().getLocation().toString());
    }



}
