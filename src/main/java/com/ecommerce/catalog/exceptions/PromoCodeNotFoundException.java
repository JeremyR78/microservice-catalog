package com.ecommerce.catalog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PromoCodeNotFoundException extends RuntimeException {

    public PromoCodeNotFoundException( Integer id ){
        super( String.format("Promo code <%s> not found !", id));
    }

    public PromoCodeNotFoundException( Integer id, String object ){
        super( String.format("Promo code <%s> not found in object <%s> !",
                id, object ));
    }

    public PromoCodeNotFoundException( Integer id, String nameClass, Integer idClass ){
        super( String.format("Promo code <%s> not found in class <%s> with id <%s> !",
                id, nameClass, idClass ));
    }

}
