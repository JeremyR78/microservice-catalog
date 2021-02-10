package com.ecommerce.catalog.sql.entity.category;

import com.ecommerce.catalog.sql.entity.common.ImageData;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name="category")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_category", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="fk_id_store")
    private Store store;

    private Boolean visible;

    @ManyToOne
    @JoinColumn(name="fk_id_category_parent")
    private Category parent;

    @NotNull
    @Column(name = "category_depth", nullable = false)
    private int depth;

    @Column(name = "ranking")
    private Integer rank;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @ManyToOne
    private ImageData imageData;

    @JoinTable(
            name = "category_tag_link",
            joinColumns = { @JoinColumn(name = "pfk_id_category") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_tag") }
    )
    @ManyToMany
    private List<Tag> tags;

    @OneToMany(mappedBy = "category" )
    private List<CategoryTranslation> categoryTranslations;

    @OneToMany( mappedBy = "category" )
    private List<CategoryProductLink> categoryProductLinkList;

}
