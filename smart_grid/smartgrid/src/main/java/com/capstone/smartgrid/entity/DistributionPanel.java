package com.capstone.smartgrid.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String dpUniqueId; // e.g., "DP-HINJ-101"

    private String name;

    @ManyToOne
    @JoinColumn(name = "substation_id")
    private Substation substation;
}