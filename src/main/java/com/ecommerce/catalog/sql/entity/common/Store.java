package com.ecommerce.catalog.sql.entity.common;

import com.ecommerce.catalog.sql.entity.price.Currency;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name="store")
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_store", nullable = false)
    private Integer id;

    @Column(length = 20)
    private String code;

    @Column(length = 100)
    private String label;

    @Column(name = "web_site", length = 100)
    private String webSite;

    @JoinTable(
            name = "store_language_link",
            joinColumns = { @JoinColumn(name = "pfk_id_store") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_language") }
    )
    @ManyToMany
    private List<Language> languages;

    @JoinTable(
            name = "store_currency_link",
            joinColumns = { @JoinColumn(name = "pfk_id_store") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_currency") }
    )
    @ManyToMany
    private List<Currency> currencies;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return Objects.equals(getId(), store.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
