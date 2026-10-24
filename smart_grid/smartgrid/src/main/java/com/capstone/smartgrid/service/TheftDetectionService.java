package com.capstone.smartgrid.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.capstone.smartgrid.entity.Pole;
import com.capstone.smartgrid.entity.Meter;
import com.capstone.smartgrid.repository.PoleRepository;
import com.capstone.smartgrid.repository.MeterRepository;
import com.capstone.smartgrid.dto.TheftAlertDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class TheftDetectionService {

    @Autowired
    private PoleRepository poleRepository;

    @Autowired
    private MeterRepository meterRepository;

    private final double ALLOWED_LINE_LOSS = 5.0; 

    public List<TheftAlertDTO> getTheftDataForDashboard() {
        List<Pole> allPoles = poleRepository.findAll();
        List<TheftAlertDTO> reportList = new ArrayList<>();

        for (Pole pole : allPoles) {
            List<Meter> connectedMeters = meterRepository.findByPoleId(pole.getId());
            
            double totalConsumed = 0;
            for (Meter meter : connectedMeters) {
                totalConsumed += meter.getConsumedPower();
                meter.setSuspect(false); 
            }

            double difference = pole.getTotalSuppliedPower() - totalConsumed;
            boolean isTheft = difference > ALLOWED_LINE_LOSS;
            String primeSuspectName = "None";

            if (isTheft) {
                double maxDrop = 0;
                Meter primeSuspect = null;
                
                for (Meter meter : connectedMeters) {
                    double drop = meter.getAverageHistoricalPower() - meter.getConsumedPower();
                    if (drop > 15 && drop > maxDrop) {
                        maxDrop = drop;
                        primeSuspect = meter;
                    }
                }
                
                if (primeSuspect != null) {
                    primeSuspect.setSuspect(true);
                    meterRepository.save(primeSuspect);
                    primeSuspectName = primeSuspect.getHouseOwnerName();
                }
            }

            pole.setTheftDetected(isTheft);
            poleRepository.save(pole);

            reportList.add(new TheftAlertDTO(
                pole.getLocationName(),
                Math.round(pole.getTotalSuppliedPower() * 100.0) / 100.0,
                Math.round(totalConsumed * 100.0) / 100.0,
                Math.round(difference * 100.0) / 100.0,
                isTheft,
                primeSuspectName
            ));
        }
        return reportList;
    }

    public String checkAllPolesForTheft() {
        List<Pole> allPoles = poleRepository.findAll();
        StringBuilder report = new StringBuilder("Theft Check Report:\n");

        for (Pole pole : allPoles) {
            List<Meter> connectedMeters = meterRepository.findByPoleId(pole.getId());
            double totalConsumed = 0;
            for (Meter meter : connectedMeters) {
                totalConsumed += meter.getConsumedPower();
            }

            double difference = pole.getTotalSuppliedPower() - totalConsumed;

            if (difference > ALLOWED_LINE_LOSS) {
                pole.setTheftDetected(true);
                report.append("ALERT: Theft detected at ").append(pole.getLocationName())
                      .append("! Power missing: ").append(difference).append(" units.\n");
            } else {
                pole.setTheftDetected(false);
                report.append("OK: ").append(pole.getLocationName()).append(" is normal.\n");
            }
            poleRepository.save(pole); 
        }
        return report.toString();
    }
}