package com.tinyurl.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedirectCreateResponse {
    private String alias;
    private String originalUrl;
    private String tinyUrl;
}
