package com.edu.degrees.api;

import com.edu.degrees.data.MenuItemCrudRepository;
import com.edu.degrees.domain.MenuItem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/menu/items")
public class MenuItemController {

    private final MenuItemCrudRepository menuItemCrudRepository;
    public MenuItemController(MenuItemCrudRepository menuItemCrudRepository) {
        this.menuItemCrudRepository = menuItemCrudRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<MenuItem>> returnMenuItem(
            UriComponentsBuilder uriComponentsBuilder) {
        Iterable<MenuItem> searchResult = menuItemCrudRepository.findAll();
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem (
            @RequestBody MenuItem menuItem, UriComponentsBuilder uriComponentsBuilder){
        MenuItem savedItem = menuItemCrudRepository.save(menuItem);
        UriComponents uriComponents =
                uriComponentsBuilder.path("/api/menu/items")
                        .buildAndExpand(savedItem.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedItem, headers, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<MenuItem> returnMenuItemId (
            @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder){
        Optional<MenuItem> searchResult = menuItemCrudRepository.findById(id);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    searchResult.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<MenuItem> deleteMenuItemId (
            @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder){
        Optional<MenuItem> searchResult = menuItemCrudRepository.findById(id);
        if (!searchResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        menuItemCrudRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("{id}")
    public ResponseEntity<MenuItem> putMenuItemId (
            @RequestBody MenuItem putMenuItem,
            @PathVariable Long id, UriComponentsBuilder uriComponentsBuilder){
        Optional<MenuItem> searchResult = menuItemCrudRepository.findById(id);
        if (!searchResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        menuItemCrudRepository.save(putMenuItem);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
