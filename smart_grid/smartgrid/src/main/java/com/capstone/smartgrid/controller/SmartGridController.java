package com.capstone.smartgrid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.capstone.smartgrid.entity.Pole;
import com.capstone.smartgrid.entity.Meter;
import com.capstone.smartgrid.repository.PoleRepository;
import com.capstone.smartgrid.repository.MeterRepository;
import com.capstone.smartgrid.service.TheftDetectionService;
import com.capstone.smartgrid.dto.TheftAlertDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SmartGridController {

    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private TheftDetectionService theftService;

    private final String[] dpAreas = {
        "Hinjewadi Phase 1 DP", 
        "Hinjewadi Phase 2 DP", 
        "Hinjewadi Phase 3 DP", 
        "Blue Ridge DP", 
        "Megapolis DP"
    };

    @GetMapping("/init-real-world")
    public String initRealWorldData() {
        meterRepository.deleteAll();
        poleRepository.deleteAll();

        Random random = new Random();
        int totalPoles = 0;
        int totalMeters = 0;

        for (String area : dpAreas) {
            for (int poleNum = 1; poleNum <= 10; poleNum++) {
                Pole pole = new Pole();
                pole.setLocationName(area + " - Pole " + poleNum);
                pole.setTotalSuppliedPower(0.0);
                pole = poleRepository.save(pole);
                totalPoles++;

                double poleSupply = 0;
                double totalConsumedByHouses = 0;
                
                // Randomly 1, 2 ya 3 mein se kisi ek ghar ko suspect banayega (sirf 15% chance ke sath)
                int theftMeterIndex = (random.nextInt(100) < 15) ? random.nextInt(3) + 1 : -1;

                for (int meterNum = 1; meterNum <= 3; meterNum++) {
                    Meter meter = new Meter();
                    // Har meter ko ek unique ID assign hogi
                    String uniqueMeterId = "MTR-" + (1000 + random.nextInt(9000));
                    meter.setHouseOwnerName(uniqueMeterId + " (House " + meterNum + " - " + area + ")");
                    
                    double historicalAverage = 30.0;
                    meter.setAverageHistoricalPower(historicalAverage);
                    
                    double consumption = 28.0 + (random.nextDouble() * 2.0); 
                    
                    if (meterNum == theftMeterIndex) { 
                        consumption = 5.0 + random.nextDouble(); 
                        poleSupply += 30.0; 
                    } else {
                        poleSupply += consumption; 
                    }
                    
                    meter.setConsumedPower(Math.round(consumption * 100.0) / 100.0);
                    meter.setPole(pole);
                    meterRepository.save(meter);
                    
                    totalConsumedByHouses += meter.getConsumedPower();
                    totalMeters++;
                }

                pole.setTotalSuppliedPower(Math.round((poleSupply + random.nextDouble() * 2) * 100.0) / 100.0);
                poleRepository.save(pole);
            }
        }

        return "Real World Data Generated! \nTotal DPs: 5 \nTotal Poles: " + totalPoles + " \nTotal Meters: " + totalMeters;
    }

    @GetMapping("/check-theft")
    public String checkTheft() {
        return theftService.checkAllPolesForTheft();
    }

    @GetMapping("/dashboard-data")
    public List<TheftAlertDTO> getDashboardData() {
        return theftService.getTheftDataForDashboard();
    }
    
    @GetMapping("/cut-power/{poleId}")
    public String cutPower(@PathVariable String poleId) {
        
        System.out.println("ACTION: Emergency Power Cut Triggered for " + poleId);
        return "SUCCESS: Power supply disconnected for " + poleId + "!";
    }
}