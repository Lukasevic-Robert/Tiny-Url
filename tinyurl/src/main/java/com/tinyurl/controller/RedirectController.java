package com.tinyurl.controller;


import com.tinyurl.dto.RedirectCreateRequest;
import com.tinyurl.service.RedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService service;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String createRedirect(@RequestBody RedirectCreateRequest request) {
        return service.createRedirect(request);
    }

    @GetMapping("/{alias}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> redirect(@PathVariable String alias) throws URISyntaxException {
        URI uri = new URI(service.getRedirect(alias));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

}
