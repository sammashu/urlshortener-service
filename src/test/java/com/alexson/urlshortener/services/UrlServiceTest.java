package com.alexson.urlshortener.services;

import com.alexson.urlshortener.config.TestRedisConfiguration;
import com.alexson.urlshortener.models.Url;
import com.alexson.urlshortener.models.exceptions.InvalidUrlException;
import com.alexson.urlshortener.models.exceptions.NoDataFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
public class UrlServiceTest {

    @Autowired
    private UrlService urlService;

    @Test(expected = NoDataFoundException.class)
    public void getOriginalUrlException(){
        urlService.getOriginalUrl("http://1234567890");
    }

    @Test
    public void generateShortUrlValidate10Character() throws NoSuchAlgorithmException, JsonProcessingException {
        Url url = new Url("","http://www.google.com", null);
        String shortUrl = urlService.getShortUrl(url);
        Assert.assertEquals(10, shortUrl.substring(shortUrl
                .lastIndexOf("/") + 1).length());
    }

    @Test(expected = InvalidUrlException.class)
    public void invalidUrlException() throws NoSuchAlgorithmException, JsonProcessingException {
        Url url = new Url("","www.google.com", null);
        urlService.getShortUrl(url);
    }
}
