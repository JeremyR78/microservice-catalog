package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PriceNotFoundException extends RuntimeException  {

    public PriceNotFoundException( Integer id ){
        super( String.format("Price <%s> not found !", id));
    }

}
