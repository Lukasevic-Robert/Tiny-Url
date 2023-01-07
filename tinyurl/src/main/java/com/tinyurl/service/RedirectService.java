package com.tinyurl.service;


import com.tinyurl.dto.RedirectCreateRequest;
import com.tinyurl.dto.RedirectCreateResponse;
import com.tinyurl.entity.Redirect;
import com.tinyurl.exception.AlreadyExistsException;
import com.tinyurl.exception.NotFoundException;
import com.tinyurl.repository.RedirectRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.tinyurl.constants.Constants.ALIAS_REGEX;
import static com.tinyurl.utils.LinkBuilder.linkTo;


@Service
@Slf4j
@RequiredArgsConstructor
public class RedirectService {

    private final RedirectRepository redirectRepository;

    public List<RedirectCreateResponse> createRedirectList(List<RedirectCreateRequest> requests) {
        List<Redirect> redirectList = new ArrayList<>();
        requests.forEach(request -> {
            String alias = optionalGenerateAliasId(request.getAlias());
            if (aliasExist(alias)) {
                throw new AlreadyExistsException("Alias: '" + alias + "' already exists in the system.");
            }
            redirectList.add(new Redirect(request.getUrl(), alias));
        });
        return redirectRepository.saveAll(redirectList).stream().map(this::toResponse).toList();
    }

    public String getRedirect(String alias) {
        return redirectRepository.findUrlByAlias(alias).orElseThrow(() -> new NotFoundException("Provided alias was not found in the system."));
    }

    public RedirectCreateResponse toResponse(Redirect redirect) {
        return new RedirectCreateResponse(redirect.getAlias(), redirect.getUrl(), linkTo(redirect.getAlias()));
    }

    private String optionalGenerateAliasId(String alias) {
        if (isValidAlias(alias)) {
            return alias;
        }
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int charactersLength = characters.length();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            result.append(characters.charAt((int) Math.floor(Math.random() * charactersLength)));
        }
        return result.toString();
    }

    private boolean isValidAlias(String alias) {
        if (alias != null && !alias.trim().isEmpty()) {
            if (!alias.matches(ALIAS_REGEX)) {
                throw new ValidationException("Alias: '" + alias + "' contains invalid characters. It should match regex pattern: " + ALIAS_REGEX);
            }
            return true;
        }
        return false;
    }

    public boolean aliasExist(String alias) {
        return redirectRepository.findUrlByAlias(alias).isPresent();
    }

}
