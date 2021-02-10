package com.ecommerce.catalog.sql.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="user_profile")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_user_profile", nullable = false)
    private Integer id;

    @JoinTable(
            name = "user_user_profile_link",
            joinColumns = { @JoinColumn(name = "pfk_id_user") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_user_profile") }
    )
    @ManyToMany
    private List<UserRole> userRoleList;



}
