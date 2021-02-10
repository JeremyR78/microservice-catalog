package com.ecommerce.catalog.sql.entity.price;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "percent")
public class Percent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_percent", nullable = false)
    private Integer id;


    private BigInteger number;


}
