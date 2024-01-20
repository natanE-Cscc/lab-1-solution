package com.edu.degrees.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesBlogController {
    @PostMapping
    public ResponseEntity createDegreesEntry(){
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
