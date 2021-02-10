package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LanguageNotFoundException extends RuntimeException {

    public LanguageNotFoundException( Integer id ){
        super( String.format("Language <%s> not found !", id));
    }

}
