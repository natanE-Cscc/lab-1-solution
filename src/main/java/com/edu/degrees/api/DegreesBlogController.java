package com.edu.degrees.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesBlogController {
    @GetMapping
    public ResponseEntity getDegrees() {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity("degrees", headers, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity createDegreesEntry() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost/api/menu/categories");
        return new ResponseEntity(null, headers, HttpStatus.CREATED);
    }
}
