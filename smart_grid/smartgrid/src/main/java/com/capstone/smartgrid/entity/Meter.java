package com.capstone.smartgrid.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String houseOwnerName;
    private double consumedPower; 
    private double averageHistoricalPower; 
    private boolean isSuspect = false; 
    
    @ManyToOne
    @JoinColumn(name = "pole_id")
    private Pole pole;
}