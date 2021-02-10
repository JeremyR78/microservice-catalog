package com.ecommerce.catalog.publisher.nosql;

import com.ecommerce.catalog.model.ActionType;
import com.ecommerce.catalog.nosql.service.ProductEsService;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.service.product.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

import static com.ecommerce.catalog.utils.logs.LogNameSpace.*;

@Getter
@RequiredArgsConstructor
public class ProductIndexCommand implements Callable<Boolean> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    private final Integer productId;
    private final ActionType action;
    private final ProductService productService;
    private final ProductEsService productEsService;

    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------



    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    public Boolean call() throws Exception {

        if( Objects.equals( this.action, ActionType.CREATED) || Objects.equals( this.action, ActionType.UPDATED) ) {

            //  CREATE OR UPDATE
            this.productEsService.createOrUpdateProduct( this.productId );
        }
        else if ( Objects.equals( this.action, ActionType.DELETED )  ){
            // DELETE
            this.productEsService.deleteProductById( this.productId );
        }

        return null;
    }
}
