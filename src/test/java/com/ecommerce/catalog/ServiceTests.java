package com.ecommerce.catalog;

import com.ecommerce.catalog.service.CatalogService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTests {


    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    @Autowired
    private CatalogService catalogService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Test
    public void test_1_service_not_null(){
        Assert.assertNotNull( "Le service n'est pas chargé ! ", this.catalogService );
    }
}
