package com.armemius.labwork.data.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "x", nullable = false)
    @NotNull
    private int x;

    @Column(name = "y", nullable = false)
    @NotNull
    private float y;

    @Column(name = "z", nullable = false)
    @NotNull
    private Double z;

    @OneToMany(mappedBy = "town", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> correspondingTowns = new ArrayList<>();

}
