package com.capstone.smartgrid.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.capstone.smartgrid.entity.Meter;
import java.util.List;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {
    List<Meter> findByPoleId(Long poleId);
}
