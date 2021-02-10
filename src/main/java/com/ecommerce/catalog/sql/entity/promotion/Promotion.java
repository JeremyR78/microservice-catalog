package com.ecommerce.catalog.sql.entity.promotion;

import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.PricePromo;
import com.ecommerce.catalog.sql.entity.tag.Tag;
import com.ecommerce.catalog.sql.entity.user.UserAction;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="promotion")
public class Promotion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_promotion", nullable = false)
    private Integer id;

    @JoinTable(
            name = "promotion_tag_link",
            joinColumns = { @JoinColumn(name = "pfk_id_promotion") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_tag") }
    )
    @ManyToMany
    private List<Tag> tags;

    private Boolean enable;

    @ManyToOne
    @JoinColumn(name = "fk_id_store")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "fk_id_country")
    private Country country;

    @ManyToOne
    @JoinColumn( name = "fk_id_customer_group")
    private CustomerGroup customerGroup;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL )
    private List<PricePromo> pricePromotions;

    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "finish_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishAt;

    @JoinTable(
            name = "promotion_user_action_link",
            joinColumns = { @JoinColumn(name = "pfk_id_promotion") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_user_action") }
    )
    @ManyToMany
    private List<UserAction> action;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

//    public boolean isEnable(){
//        if( this.getPriceWithPromotion() != null
//                && this.getPriceWithPromotion().getCurrentPrice() )
//        {
//            return true;
//        }
//        return false;
//    }
//
//    public void setEnable( boolean enable ){
//        if(  this.getPriceWithPromotion() != null ) {
//            this.getPriceWithPromotion().setCurrentPrice( enable );
//        }
//    }

    /**
     * Vérification de l'activation de la promotion
     *
     * @return
     */
    public boolean isEnablePromotion( Date currentDate ){
        return this.getEnable()
                && this.getFinishAt() != null
                && this.getStartAt() != null
                && this.getStartAt().before(currentDate)
                && this.getFinishAt().after(currentDate);
    }


    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", enable=" + enable +
                '}';
    }
}
