package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StoreNotFoundException extends RuntimeException {

    public StoreNotFoundException( Integer id ){
        super( String.format("Store <%s> not found !", id));
    }

    public StoreNotFoundException( Integer id, String object ){
        super( String.format("Store <%s> not found in object <%s> !",
                id, object ));
    }

    public StoreNotFoundException( Integer id, String nameClass, Integer idClass ){
        super( String.format("Store <%s> not found in class <%s> with id <%s> !",
                id, nameClass, idClass ));
    }

}
