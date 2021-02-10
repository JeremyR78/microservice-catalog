package com.ecommerce.catalog.exceptions;

import com.ecommerce.catalog.sql.entity.product.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PricePromoNotCreatedException extends RuntimeException  {

    public PricePromoNotCreatedException( Integer id ){
        super( String.format("Fail to create PricePromo <%s> !", id));
    }

    public PricePromoNotCreatedException( Product product ){
        super( String.format("Fail to create PricePromo in Product <%s> !", product.getId() ));
    }

}
