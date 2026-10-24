package com.capstone.smartgrid.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.capstone.smartgrid.entity.DistributionPanel;

@Repository
public interface DistributionPanelRepository extends JpaRepository<DistributionPanel, Long> {
}