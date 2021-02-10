package com.ecommerce.catalog.sql.entity.common;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="image")
public class ImageData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_image", nullable = false)
    private Integer id;
    private String label;
    private String filename;

    private Double size;
    private String hash;

    private PathDisk pathDisk;

}
