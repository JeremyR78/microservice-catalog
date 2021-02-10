package com.ecommerce.catalog.sql.entity.user;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name="user_role")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_user_role", nullable = false)
    private Integer id;

    private String role;

    private String label;

    private String description;

}
