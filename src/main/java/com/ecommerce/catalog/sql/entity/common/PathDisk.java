package com.ecommerce.catalog.sql.entity.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class PathDisk implements Serializable {

    private Integer id;
    private String label;
    private String path;
    private String url;

}
