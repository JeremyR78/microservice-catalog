package com.ecommerce.catalog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractController {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    public final static String APPLICATION_JSON_VALUE_UTF8 = "application/json; charset=UTF-8";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    protected Logger getLogger(){
        return this.logger;
    }

}
