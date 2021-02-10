package com.ecommerce.catalog.sql.entity.promotion;

import com.ecommerce.catalog.model.PriceUnitType;
import com.ecommerce.catalog.sql.entity.common.Country;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.user.UserAction;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="promo_code")
public class PromoCode implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_promo_code", nullable = false)
    private Integer id;

    private Boolean enable;

    private String code;

    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "fk_id_curency")
    private Currency currency;

    private PriceUnitType priceUnitType;

    @ManyToOne
    @JoinColumn(name = "fk_id_store")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "fk_id_country")
    private Country country;

    @ManyToOne
    @JoinColumn( name = "fk_id_customer_group")
    private CustomerGroup customerGroup;

    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "finish_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishAt;



    @JoinTable(
            name = "promo_code_user_action_link",
            joinColumns = { @JoinColumn(name = "pfk_id_promo_code") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_user_action") }
    )
    @ManyToMany
    private List<UserAction> action;


}
