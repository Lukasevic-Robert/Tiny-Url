package com.tinyurl.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import static com.tinyurl.constants.Constants.ALIAS_REGEX;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "redirect")
public class Redirect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @NaturalId
    @Column(unique = true, nullable = false)
    @Pattern(regexp = ALIAS_REGEX)
    private String alias;

    public Redirect(String url, String alias) {
        this.url = url;
        this.alias = alias;
    }
}
