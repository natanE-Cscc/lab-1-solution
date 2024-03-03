package com.edu.degrees.api;

import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.domain.MenuCategory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DegreesBlogControllerIT {
    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestRestTemplate restTemplate;
    private static final String RESOURCE_URI = "http://localhost:%d/api/menu/categories";
    private static final MenuCategory testPosting = new MenuCategory(0L,"categoryTitle", "categoryNotes", 6 );
    private static final MenuCategory savedPosting = new MenuCategory(1L,"categoryTitle", "categoryNotes", 6 );


    @Test
    @DisplayName("Post includes server port")
        public void test01() {
            ResponseEntity<MenuCategory> responseEntity =
                    this.restTemplate.postForEntity(String.format(RESOURCE_URI, localServerPort), savedPosting, MenuCategory.class);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(localServerPort, responseEntity.getHeaders().getLocation().getPort());
    }
    @Test
    @DisplayName("Post generates nonzero ID")
    public void test02() {
        ResponseEntity<MenuCategory> responseEntity =
                this.restTemplate.postForEntity(String.format(RESOURCE_URI, localServerPort), testPosting, MenuCategory.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        MenuCategory actualPost = responseEntity.getBody();
        assertNotEquals(testPosting.getId(), actualPost.getId());
        assertEquals(String.format(RESOURCE_URI + "/%d", localServerPort, actualPost.getId())
                ,responseEntity.getHeaders().getLocation().toString());
    }

}
