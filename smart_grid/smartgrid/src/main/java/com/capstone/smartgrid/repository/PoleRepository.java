package com.capstone.smartgrid.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.capstone.smartgrid.entity.Pole;

@Repository
public interface PoleRepository extends JpaRepository<Pole, Long> {
}