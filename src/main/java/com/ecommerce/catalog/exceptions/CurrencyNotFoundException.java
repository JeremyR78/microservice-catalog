package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyNotFoundException extends RuntimeException{

    public CurrencyNotFoundException( Integer id ){
        super( String.format("Currency <%s> not found !", id));
    }

    public CurrencyNotFoundException( Integer id, String object ){
        super( String.format("Currency <%s> not found in object <%s> !",
                id, object ));
    }

    public CurrencyNotFoundException( Integer id, String nameClass, Integer idClass ){
        super( String.format("Currency <%s> not found in class <%s> with id <%s> !",
                id, nameClass, idClass ));
    }

}
