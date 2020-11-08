package com.alexson.urlshortener.models.exceptions;

import com.alexson.urlshortener.models.ResponseError;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(ResponseError responseError){
        super(String.format(responseError.getValue() + " " + responseError.getMsg()));
    }
}
