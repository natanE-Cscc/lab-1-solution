package com.edu.degrees.api;

import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.domain.MenuCategory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesBlogController {

    private final DegreesBlogRepository degreesBlogRepository;

    public DegreesBlogController(DegreesBlogRepository degreesBlogRepository) {
        this.degreesBlogRepository=degreesBlogRepository;
    }

    @PostMapping
    public ResponseEntity <MenuCategory> createDegreesEntry(
            @RequestBody MenuCategory menuCategory, UriComponentsBuilder uriComponentsBuilder) {
        MenuCategory savedPost = degreesBlogRepository.save(menuCategory);
        HttpHeaders headers = new HttpHeaders();
      UriComponents uriComponents = uriComponentsBuilder.path("/api/menu/categories/{id}").buildAndExpand(savedPost.getId());
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedPost, headers, HttpStatus.CREATED);
    }
}
