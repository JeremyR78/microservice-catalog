package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CountryNotFoundException extends RuntimeException  {

    public CountryNotFoundException( Integer id ) {
        super( String.format("Country <%s> not found !", id));
    }

    public CountryNotFoundException( Integer id, String object ){
        super( String.format("Country <%s> not found in object <%s> !",
                id, object ));
    }

    public CountryNotFoundException( Integer id, String nameClass, Integer idClass ){
        super( String.format("Country <%s> not found in class <%s> with id <%s> !",
                id, nameClass, idClass ));
    }

}
