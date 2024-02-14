package com.edu.degrees.api;

import com.edu.degrees.data.MenuItemRepository;
import com.edu.degrees.domain.MenuItem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu/items")
public class MenuItemController {
    private final MenuItemRepository menuItemRepository;


    public MenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }
    @PostMapping
    public ResponseEntity<MenuItem> createItemEntry(
            @RequestBody MenuItem menuItem, UriComponentsBuilder uriComponentsBuilder) {
        MenuItem savedPost = menuItemRepository.save(menuItem);
        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents = uriComponentsBuilder.path("/api/menu/items/{id}").buildAndExpand(savedPost.getId());
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedPost, headers, HttpStatus.CREATED);
    }
    @GetMapping
    public Iterable<MenuItem> getItemEntries() {
        return menuItemRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Iterable<MenuItem>> getItemByID(@PathVariable Long id) {
        Optional<MenuItem> searchResult = menuItemRepository.findById(id);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    Collections.singletonList(searchResult.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("{id}")
    public ResponseEntity<MenuItem> updateItemEntry(@PathVariable Long id,
                                                            @RequestBody MenuItem itemEntry) {
        if (itemEntry.getId() != id) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if(menuItemRepository.existsById(id)) {
            menuItemRepository.save(itemEntry);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MenuItem> deleteItemById(@PathVariable Long id) {
        Optional<MenuItem> itemEntry = menuItemRepository.findById(id);
        if(itemEntry.isPresent()) {
            menuItemRepository.delete(itemEntry.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
