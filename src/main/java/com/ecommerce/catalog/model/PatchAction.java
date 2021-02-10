package com.ecommerce.catalog.model;

import lombok.Getter;

public enum PatchAction {

    ADD("add"),
    COPY("copy"),
    MOVE("move"),
    REMOVE("remove"),
    REPLACE("replace"),
    TEST("test");

    @Getter
    private final String label;

    PatchAction( String label ){
        this.label = label;
    }

}
