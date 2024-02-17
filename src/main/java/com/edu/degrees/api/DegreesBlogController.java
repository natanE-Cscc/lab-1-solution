package com.edu.degrees.api;

import com.edu.degrees.data.DegreesBlogRepository;
import com.edu.degrees.domain.MenuCategory;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesBlogController {

    private final DegreesBlogRepository degreesBlogRepository;

    public DegreesBlogController(DegreesBlogRepository degreesBlogRepository) {
        this.degreesBlogRepository=degreesBlogRepository;
    }

    @PostMapping
    public ResponseEntity <MenuCategory> createDegreesEntry(
            @Valid @RequestBody MenuCategory menuCategory, UriComponentsBuilder uriComponentsBuilder) {
        MenuCategory savedPost = degreesBlogRepository.save(menuCategory);
        HttpHeaders headers = new HttpHeaders();
      UriComponents uriComponents = uriComponentsBuilder.path("/api/menu/categories/{id}").buildAndExpand(savedPost.getId());
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedPost, headers, HttpStatus.CREATED);
    }
    @GetMapping
    public Iterable<MenuCategory> getCategoryEntries() {
        return degreesBlogRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Iterable<MenuCategory>> getCategoryByID(@PathVariable Long id) {
        Optional<MenuCategory> searchResult = degreesBlogRepository.findById(id);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    Collections.singletonList(searchResult.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("{id}")
    public ResponseEntity<MenuCategory> updateCategoryEntry(@PathVariable Long id,
                                                            @Valid @RequestBody MenuCategory categoryEntry) {
        if (categoryEntry.getId() != id) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(degreesBlogRepository.existsById(id)) {
            degreesBlogRepository.save(categoryEntry);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<MenuCategory> deleteCategoryById(@PathVariable Long id) {
        Optional<MenuCategory> categoryEntry = degreesBlogRepository.findById(id);
        if(categoryEntry.isPresent()) {
            degreesBlogRepository.delete(categoryEntry.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
