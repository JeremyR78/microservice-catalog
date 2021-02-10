package com.ecommerce.catalog.service;

import com.ecommerce.catalog.dto.product.ProductView;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoSqlService {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------


    public boolean update( ProductView productView ){

        return true;
    }

}
