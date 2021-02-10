package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerGroupNotFoundException extends RuntimeException {

    public CustomerGroupNotFoundException( Integer id ){
        super( String.format("Customer group <%s> not found !", id));
    }

    public CustomerGroupNotFoundException( Integer id, String object ){
        super( String.format("Customer group <%s> not found in object <%s> !",
                id, object ));
    }

    public CustomerGroupNotFoundException( Integer id, String nameClass, Integer idClass ){
        super( String.format("Customer group <%s> not found in class <%s> with id <%s> !",
                id, nameClass, idClass ));
    }
}
