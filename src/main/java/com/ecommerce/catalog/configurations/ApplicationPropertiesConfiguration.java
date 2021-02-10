package com.ecommerce.catalog.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mes-configs")
public class ApplicationPropertiesConfiguration {

    private int limitDeProducts;

    public int getLimitDeProducts() {
        return limitDeProducts;
    }

    public void setLimitDeProducts(int limitDeProducts) {
        this.limitDeProducts = limitDeProducts;
    }


}
