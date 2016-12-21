package com.johnremboo.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Entity
@Getter
@Setter
@Table(name = "contacts", schema = "users")
public class Contact {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @NotNull
    @Column(name = "name", length = 100)
    private String name;

    public Contact() {
        // no operations
    }
}
