package com.ecommerce.catalog.sql.entity.user;

import com.ecommerce.catalog.model.ActionType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="user_action")
public class UserAction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_user_action", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="fk_id_user_id")
    private User userId;

    @Column(name="action_type")
    private ActionType actionType;

    @Column(name = "at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String notes;

    private String justification;

}
