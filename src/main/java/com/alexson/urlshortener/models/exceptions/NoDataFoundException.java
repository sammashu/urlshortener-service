package com.alexson.urlshortener.models.exceptions;

import com.alexson.urlshortener.models.ResponseError;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException(ResponseError responseError){
        super(String.format(responseError.getField() + " : " + responseError.getValue() + " " + responseError.getMsg()));
    }
}
