package com.tinyurl.controller;

import com.tinyurl.dto.RedirectCreateResponse;
import com.tinyurl.service.RedirectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class RedirectControllerTest {

    @MockBean
    RedirectService redirectService;
    @Autowired
    MockMvc mockMvc;

    private static final String LONG_URL = "https://example-long-url.lt/";
    private static final String TINY_URL = "http://localhost/";
    private static final String ALIAS = "alias";
    private static final String RESPONSE = "[{\"alias\":\"alias\",\"originalUrl\":\"https://example-long-url.lt/\",\"tinyUrl\":\"http://localhost/alias\"}]";
    private static final String REQUEST = """
            [
                {
                    "alias": "alias",
                    "url": "https://example-long-url.lt/"
                }
            ]""";

    @Test
    void createRedirectWhenOk() throws Exception {
        when(redirectService.createRedirectList(any())).thenReturn(List.of(createResponse()));

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST))
                .andExpect(status().isCreated())
                .andExpect(content().string(RESPONSE));
    }

    @Test
    void redirectWhenOk() throws Exception {
        when(redirectService.getRedirect(ALIAS)).thenReturn(LONG_URL);

        mockMvc.perform(get("/" + ALIAS))
                .andExpect(status().isMovedPermanently())
                .andExpect(header().string("Location", LONG_URL));
    }

    private RedirectCreateResponse createResponse() {
        return new RedirectCreateResponse(ALIAS, LONG_URL, TINY_URL + ALIAS);
    }
}
