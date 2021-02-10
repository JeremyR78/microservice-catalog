package com.ecommerce.catalog.sql.entity.price;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "currency")
public class Currency implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_currency", nullable = false)
    private Integer id;
    private String symbol;
    private String label;


    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
