package com.tinyurl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RedirectCreateRequest {
    @Schema(example = "takeMeToGoogle")
    private String alias;
    @Schema(example = "https://google.com/")
    private String url;
}
