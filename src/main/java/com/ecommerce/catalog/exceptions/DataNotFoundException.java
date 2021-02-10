package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends Exception {

    public DataNotFoundException( Class classType, String key, String value ){
        super( String.format("In the class <%s> not found the value <%s> for the key <%s> !",
                classType, key, value ));
    }

}
