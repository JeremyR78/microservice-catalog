package com.ecommerce.catalog.sql.entity.tag;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Annotation à ajouter sur un produit
 *
 * ex : PROMO, SOLDE, NOUVEAU, VENTE FLASH
 */
@Data
@Entity
@Table(name="tag")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_tag", nullable = false)
    private Integer id;

    private String label;

    @OneToMany(mappedBy = "tagParent")
    private List<TagLink> patentsTags;

    @OneToMany(mappedBy = "tagChild")
    private List<TagLink> childrenTags;

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
