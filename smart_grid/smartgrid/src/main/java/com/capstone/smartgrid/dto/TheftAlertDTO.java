package com.capstone.smartgrid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheftAlertDTO {
    private String poleLocation;
    private double suppliedPower;
    private double consumedPower;
    private double powerLoss;
    private boolean theftDetected;
    private String suspectedMeter;
}