package com.tinyurl.exception;


import com.tinyurl.controller.RedirectController;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.tinyurl.constants.Constants.ALIAS_REGEX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class GlobalControllerExceptionHandlerTest {

    @MockBean
    RedirectController redirectController;
    @Autowired
    MockMvc mockMvc;
    private static final String REQUEST = """
            [
                {
                    "alias": "alias",
                    "url": "https://example-long-url.lt/"
                }
            ]""";
    private static final String ALIAS = "alias";
    private static final String INVALID_ALIAS = "invalid+alias";
    private static final String VALIDATION_ERROR_MESSAGE = "Alias: '" + INVALID_ALIAS + "' contains invalid characters. It should match regex pattern: " + ALIAS_REGEX;
    private static final String NOT_FOUND_EXCEPTION_ERROR_MESSAGE = "Provided alias was not found in the system.";
    private static final String ALREADY_EXISTS_EXCEPTION_ERROR_MESSAGE = "Alias: '" + ALIAS + "' already exists in the system.";
    private static final String EXCEPTION_ERROR_MESSAGE = "Internal server error message.";

    @Test
    void createRedirect_Catch_ValidationException() throws Exception {
        when(redirectController.createRedirect(any())).thenThrow(new ValidationException(VALIDATION_ERROR_MESSAGE));

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"" + VALIDATION_ERROR_MESSAGE + "\"}"));
    }

    @Test
    void createRedirect_Catch_AlreadyExistsException() throws Exception {
        when(redirectController.createRedirect(any())).thenThrow(new AlreadyExistsException(ALREADY_EXISTS_EXCEPTION_ERROR_MESSAGE));

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST))
                .andExpect(status().isConflict())
                .andExpect(content().string("{\"message\":\"" + ALREADY_EXISTS_EXCEPTION_ERROR_MESSAGE + "\"}"));
    }

    @Test
    void getRedirect_Catch_NotFoundException() throws Exception {
        when(redirectController.redirect(any())).thenThrow(new NotFoundException(NOT_FOUND_EXCEPTION_ERROR_MESSAGE));

        mockMvc.perform(get("/alias"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"message\":\"" + NOT_FOUND_EXCEPTION_ERROR_MESSAGE + "\"}"));
    }

    @Test
    void getRedirect_Catch_Exception() throws Exception {
        when(redirectController.redirect(any())).thenThrow(new RuntimeException(EXCEPTION_ERROR_MESSAGE));

        mockMvc.perform(get("/alias"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"message\":\"" + EXCEPTION_ERROR_MESSAGE + "\"}"));
    }
}
