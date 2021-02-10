package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PropertyNotFoundException extends RuntimeException {

    public PropertyNotFoundException( Integer id ){
        super( String.format("Property <%s> not found !", id));
    }

}
