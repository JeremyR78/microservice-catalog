package com.ecommerce.catalog.controller;

import com.ecommerce.catalog.sql.entity.product.Product;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HealthController extends AbstractController implements HealthIndicator  {

    @Override
    public Health health() {

        //List<Product> products = productService.findAll();
        List<Product> products = new ArrayList<>();

        if(products.isEmpty()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}
