package com.tinyurl.controller;


import com.tinyurl.dto.RedirectCreateRequest;
import com.tinyurl.dto.RedirectCreateResponse;
import com.tinyurl.service.RedirectService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService service;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public List<RedirectCreateResponse> createRedirect(@RequestBody List<RedirectCreateRequest> requests) {
        return service.createRedirectList(requests);
    }

    @GetMapping("/{alias}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<HttpHeaders> redirect(@Parameter(description = "Existing alias string that is wired to original url.", example = "takeMeToGoogle") @PathVariable String alias) throws URISyntaxException {
        URI uri = new URI(service.getRedirect(alias));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }

}
