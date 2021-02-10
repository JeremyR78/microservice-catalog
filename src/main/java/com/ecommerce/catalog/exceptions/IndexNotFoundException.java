package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IndexNotFoundException extends RuntimeException {

    public IndexNotFoundException( String index ){
        super( String.format("Index <%s> not found !", index ));
    }

}
