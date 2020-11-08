package com.alexson.urlshortener.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Url implements Serializable {
    private String shortUrl;
    private String url;
    private LocalDateTime creationDateTime;
}
