package com.ecommerce.catalog.sql.entity.common;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name="language")
public class Language implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_language", nullable = false)
    private Integer id;

    @Column(name = "iso_code")
    private String isoCode;
    private Boolean active;

    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL )
    private List<LanguageTranslation> languageTranslationList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;
        Language language = (Language) o;
        return Objects.equals(getId(), language.getId()) &&
                Objects.equals(getIsoCode(), language.getIsoCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getIsoCode());
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", isoCode='" + isoCode + '\'' +
                '}';
    }
}
