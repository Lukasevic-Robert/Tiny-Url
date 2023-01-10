package com.tinyurl.utils;

import com.tinyurl.controller.RedirectController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

public class LinkBuilder {

    private LinkBuilder() {
    }

    private static final String BASE_URI = MvcUriComponentsBuilder.fromController(RedirectController.class).build().toString();

    public static String linkTo(String alias) {
        return BASE_URI + alias;
    }
}
