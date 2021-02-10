package com.ecommerce.catalog.sql.entity.customer;

import com.ecommerce.catalog.model.CustomerGroupType;
import com.ecommerce.catalog.sql.entity.user.UserAction;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "customer_group")
public class CustomerGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_customer_group", nullable = false)
    private Integer id;

    private CustomerGroupType type;

    private String label;

    @JoinTable(
            name = "customer_group_user_action_link",
            joinColumns = { @JoinColumn(name = "pfk_id_customer_group") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_user_action") }
    )
    @ManyToMany
    private List<UserAction> userActions;


    @Override
    public String toString() {
        return "CustomerGroup{" +
                "id=" + id +
                ", type=" + type +
                ", label='" + label + '\'' +
                '}';
    }
}
