package com.capstone.smartgrid.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.capstone.smartgrid.entity.Substation;

@Repository
public interface SubstationRepository extends JpaRepository<Substation, Long> {
}
