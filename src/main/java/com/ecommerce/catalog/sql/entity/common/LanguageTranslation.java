package com.ecommerce.catalog.sql.entity.common;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "language_translation")
public class LanguageTranslation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_language_translation", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="fk_id_language")
    private Language language;

    @ManyToOne
    @JoinColumn(name="fk_id_language_reference")
    private Language languageReference;

    private String label;



}
