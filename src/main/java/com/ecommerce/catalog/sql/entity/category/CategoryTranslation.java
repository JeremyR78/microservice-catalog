package com.ecommerce.catalog.sql.entity.category;

import com.ecommerce.catalog.sql.entity.common.Language;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="category_translation")
@IdClass(CategoryTranslationID.class)
public class CategoryTranslation implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_category")
    @ManyToOne(optional = false)
    private Category category;

    @Id
    @JoinColumn(name = "pfk_id_language")
    @ManyToOne(optional = false)
    private Language language;

    private String label;
}
