package com.tinyurl.service;

import com.tinyurl.dto.RedirectCreateRequest;
import com.tinyurl.dto.RedirectCreateResponse;
import com.tinyurl.entity.Redirect;
import com.tinyurl.exception.AlreadyExistsException;
import com.tinyurl.repository.RedirectRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.tinyurl.constants.Constants.ALIAS_REGEX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RedirectServiceTest {

    @InjectMocks
    RedirectService redirectService;
    @Mock
    RedirectRepository mockRepository;

    private static final String LONG_URL = "https://example-long-url.lt/";
    private static final String TINY_URL = "http://localhost/";
    private static final String ALIAS = "alias";
    private static final String INVALID_ALIAS = "invalid+alias";

    @Test
    void getRedirectByAlias() {
        when(mockRepository.findUrlByAlias(anyString())).thenReturn(Optional.of("url"));

        assertEquals("url", redirectService.getRedirect(anyString()));
    }

    @Test
    void createRedirectListWhenOk() {
        when(mockRepository.saveAll(any())).thenReturn(List.of(createRedirect()));
        when(mockRepository.findUrlByAlias(anyString())).thenReturn(Optional.empty());
        RedirectCreateResponse redirectCreateResponse = createResponse();
        List<RedirectCreateResponse> responseList = redirectService.createRedirectList(List.of(createRequest()));

        assertEquals(redirectCreateResponse.getTinyUrl(), responseList.get(0).getTinyUrl());
    }

    @Test
    void createRedirectList_Expect_AlreadyExistsException_When_AliasFoundInTheSystem() {
        when(mockRepository.findUrlByAlias(anyString())).thenReturn(Optional.of(anyString()));
        List<RedirectCreateRequest> requestList = List.of(createRequest());

        Exception exception = assertThrows(AlreadyExistsException.class, () -> redirectService.createRedirectList(requestList));

        assertEquals("Alias: '" + ALIAS + "' already exists in the system.", exception.getMessage());
    }

    @Test
    void createRedirectList_Expect_ValidationException_When_AliasContainsInvalidSymbols() {
        RedirectCreateRequest request = createRequest();
        request.setAlias(INVALID_ALIAS);
        List<RedirectCreateRequest> requestList = List.of(request);

        Exception exception = assertThrows(ValidationException.class, () -> redirectService.createRedirectList(requestList));

        assertEquals("Alias: '" + INVALID_ALIAS + "' contains invalid characters. It should match regex pattern: " + ALIAS_REGEX, exception.getMessage());
    }

    @Test
    void validateOrGenerateAliasId_Expect_generateRandomAlias_When_NoAliasProvided() {
        when(mockRepository.findUrlByAlias(anyString())).thenReturn(Optional.empty());
        String generatedAlias = redirectService.validateOrGenerateAliasId("");

        verify(mockRepository, times(1)).findUrlByAlias(generatedAlias);
    }

    private Redirect createRedirect() {
        return new Redirect(LONG_URL, ALIAS);
    }

    private RedirectCreateRequest createRequest() {
        return new RedirectCreateRequest(ALIAS, LONG_URL);
    }

    private RedirectCreateResponse createResponse() {
        return new RedirectCreateResponse(ALIAS, LONG_URL, TINY_URL + ALIAS);
    }
}
