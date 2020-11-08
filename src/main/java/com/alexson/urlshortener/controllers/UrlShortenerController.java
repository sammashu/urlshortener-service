package com.alexson.urlshortener.controllers;

import com.alexson.urlshortener.models.Url;
import com.alexson.urlshortener.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {

    private UrlService urlService;

    @Autowired
    public UrlShortenerController(UrlService urlService){
        this.urlService = urlService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity getOriginalUrl(@RequestParam String shortUrl){
        return ResponseEntity.ok(urlService.getOriginalUrl(shortUrl));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity getShortUrl(@RequestBody @NotNull Url url) throws NoSuchAlgorithmException {
        return  ResponseEntity.ok(urlService.getShortUrl(url));
    }
}
