package com.ecommerce.catalog.model;

import lombok.Getter;

public enum PropertyType {

    BOOLEAN( "BOOLEAN" ),
    NUMBER("NUMBER"),
    STRING("STRING");

    @Getter
    private final String value;

    PropertyType( String value ){
        this.value = value;
    }

}
