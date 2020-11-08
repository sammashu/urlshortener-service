package com.alexson.urlshortener.services;

import com.alexson.urlshortener.models.ResponseError;
import com.alexson.urlshortener.models.Url;
import com.alexson.urlshortener.models.exceptions.InvalidUrlException;
import com.alexson.urlshortener.models.exceptions.NoDataFoundException;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class UrlService {

    private RedisTemplate<String, Url> redisTemplate;

    @Autowired
    public UrlService(RedisTemplate<String, Url> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public Url getOriginalUrl(String shortUrl){
        final Url originalUrl = redisTemplate.opsForValue().get(shortUrl);

        if(null == originalUrl){
            final ResponseError responseError = new ResponseError("shortUrl", shortUrl, "The original Url can't be found");
            throw new NoDataFoundException(responseError);
        }
        return originalUrl;
    }

    public String getShortUrl(Url url) throws NoSuchAlgorithmException {
        String shortUrl = "";

        final UrlValidator urlValidator = new UrlValidator(
          new String[]{"http", "https"}
        );

        if (!urlValidator.isValid(url.getUrl())) {
            final ResponseError responseError = new ResponseError("", url.getUrl(), "Invalid URL");
            throw new InvalidUrlException(responseError);
        }

        shortUrl = returnShortUrlIfOriginalUrlExist(url);
        if(null == shortUrl) {
            generateShortUrl(url);
            url.setCreationDateTime(LocalDateTime.now());
            redisTemplate.opsForValue().set(url.getUrl(), url);
            shortUrl = url.getShortUrl();
            redisTemplate.opsForValue().set(shortUrl, url);
        }

        return shortUrl;
    }

    private void generateShortUrl(Url url) throws NoSuchAlgorithmException {
        if(url.getUrl().contains("https")) {
            url.setShortUrl("https://" + String.format("%032x", getIntUrl(url)).substring(0, 10)); // turn to hexadecimal
        }else{
            url.setShortUrl("http://" + String.format("%032x", getIntUrl(url)).substring(0, 10));
        }
        Url shortUrl = redisTemplate.opsForValue().get(url.getShortUrl());
        if(null != shortUrl){ // recall the function in case generating short url exist already just collision check
            generateShortUrl(url);
        }
    }

    private BigInteger getIntUrl(Url url) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] urlByte = messageDigest.digest(url.getUrl().getBytes());
        BigInteger intUrlSHA256 = new BigInteger(1, urlByte);
        return intUrlSHA256;
    }

    private String returnShortUrlIfOriginalUrlExist(Url url) {
        if(null != redisTemplate.opsForValue().get(url.getUrl())){
            Url shortUrlFromOriginalUrl = redisTemplate.opsForValue().get(url.getUrl());
            return shortUrlFromOriginalUrl.getShortUrl();
        }
        return null;

    }
}
