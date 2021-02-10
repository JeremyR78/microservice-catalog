package com.ecommerce.catalog.nosql.model;


public class EsIndexChange {
    private static String indexName;

    public static String getIndexName() {
        return indexName;
    }

    public static void setIndexName(String indexName) {
        EsIndexChange.indexName = indexName;
    }
}
