package com.ecommerce.catalog.sql.entity.category;

import com.ecommerce.catalog.sql.entity.product.Product;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="category_product_link")
public class CategoryProductLink implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_product")
    @ManyToOne(optional = false)
    private Product product;

    @Id
    @JoinColumn(name = "pfk_id_category")
    @ManyToOne(optional = false)
    private Category category;

    private Integer rank;

}
