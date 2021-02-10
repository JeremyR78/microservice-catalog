package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PropertyUnitNotFoundException extends RuntimeException {

    public PropertyUnitNotFoundException( Integer id ){
        super( String.format("Property unit <%s> not found !", id));
    }

}
