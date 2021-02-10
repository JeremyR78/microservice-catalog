package com.ecommerce.catalog.model;

import lombok.Getter;

public enum  PriceUnitType {

    PERCENT( "Percent"),
    CASH("Cash");

    @Getter
    private final String label;

    PriceUnitType( String label ){
        this.label = label;
    }


}
