package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PromotionNotFoundException extends RuntimeException  {

    public PromotionNotFoundException( Integer id ){
        super( String.format("Promotion <%s> not found !", id));
    }

}
