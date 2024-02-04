package com.edu.degrees.api;

import com.edu.degrees.data.MenuCategoryRepository;
import com.edu.degrees.domain.MenuCategory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesRestController {

    private final MenuCategoryRepository menuCategoryRepository;

    public DegreesRestController(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @PostMapping
    public ResponseEntity<MenuCategory> createMenuEntry(
            @RequestBody MenuCategory menuCategory, UriComponentsBuilder uriComponentsBuilder) {
        MenuCategory savedItem = menuCategoryRepository.save(menuCategory);
        UriComponents uriComponents =
                uriComponentsBuilder.path("/api/menu/categories/{id}")
                        .buildAndExpand(savedItem.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedItem, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<MenuCategory>> returnMenuEntry(
            UriComponentsBuilder uriComponentsBuilder) {
        Iterable<MenuCategory> searchResult = menuCategoryRepository.findAll();
        return new ResponseEntity<>(searchResult, HttpStatus.FOUND);
    }

    @GetMapping("{id}")
    public ResponseEntity<MenuCategory> returnMenuEntryId(
            @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {
        Optional<MenuCategory> searchResult = menuCategoryRepository.findById(id);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    searchResult.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MenuCategory> deleteMenuEntryId(
            @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {
        Optional<MenuCategory> searchResult = menuCategoryRepository.findById(id);
        if (!searchResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        menuCategoryRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{id}")
    public ResponseEntity<MenuCategory> putMenuEntryId(
            @RequestBody MenuCategory putMenuCategory,
            @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {
        Optional<MenuCategory> searchResult = menuCategoryRepository.findById(id);
        if (!searchResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        menuCategoryRepository.save(putMenuCategory);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
