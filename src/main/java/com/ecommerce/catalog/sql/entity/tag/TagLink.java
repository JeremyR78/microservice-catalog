package com.ecommerce.catalog.sql.entity.tag;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Define the link between two tag
 *
 */
@Data
@Entity
@Table(name="tag_link")
@IdClass( TagLinkID.class )
public class TagLink implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_tag_parent", nullable = false)
    @ManyToOne(optional = false)
    private Tag tagParent;

    @Id
    @JoinColumn(name = "pfk_id_tag_child", nullable = false)
    @ManyToOne(optional = false)
    private Tag tagChild;


    /**
     * Link strength
     *
     * This information is used for analyse the recommendation
     *
     */
    private Integer linkStrength;


    @Override
    public String toString() {
        return "TagLink{" +
                "tagParent=" + tagParent +
                ", tagChild=" + tagChild +
                ", linkStrength=" + linkStrength +
                '}';
    }
}
