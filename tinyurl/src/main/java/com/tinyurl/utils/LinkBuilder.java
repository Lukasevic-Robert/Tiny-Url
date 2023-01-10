package com.tinyurl.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class LinkBuilder {

    private LinkBuilder() {
    }

    public static String linkTo(String alias) {
        String baseUri = ServletUriComponentsBuilder.fromCurrentServletMapping().build().toString();
        return baseUri + "/" + alias;
    }
}
