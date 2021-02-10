package com.ecommerce.catalog.sql.entity.product;

import com.ecommerce.catalog.sql.entity.common.Language;
import com.ecommerce.catalog.sql.entity.common.Store;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "product_translation")
@IdClass( ProductTranslationID.class )
public class ProductTranslation implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_product", nullable = false)
    @ManyToOne
    private Product product;

    @Id
    @JoinColumn(name = "pfk_id_language", nullable = false)
    @ManyToOne
    private Language language;

    @Id
    @JoinColumn(name = "pfk_id_store", nullable = false)
    @ManyToOne
    private Store store;

    private String model;
    private String presentation;
    private String description;


    @Override
    public String toString() {
        return "ProductTranslation{" +
                "product=" + product +
                ", model='" + model + '\'' +
                '}';
    }
}
