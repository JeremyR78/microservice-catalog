package com.ecommerce.catalog.nosql.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;


@Data
@Document( indexName = "#{ T(com.ecommerce.catalog.nosql.model.EsIndexChange).getIndexName() }", createIndex = false )
public class ProductEs implements Serializable {

    @Id
    private Integer id;
    private String reference;

    //
    // DESCRIPTION
    //
    private String model;
    private String presentation;
    private String description;

    //
    //  PRICES
    //
    private List<PriceEs>  prices;

    //
    //  PROPERTIES
    //
    private List<PropertyEs> properties;

    //
    //  CATEGORIES
    //
    private List<CategoryEs> categoryEs;

}
