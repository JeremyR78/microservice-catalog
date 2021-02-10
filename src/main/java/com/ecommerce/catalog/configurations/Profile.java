package com.ecommerce.catalog.configurations;

import lombok.Getter;

public enum Profile {
    ELASTICSEARCH("elasticsearch");

    @Getter
    private final String label;

    private Profile( String label ){
        this.label = label;
    }


    @Override
    public String toString() {
        return  label;
    }
}
