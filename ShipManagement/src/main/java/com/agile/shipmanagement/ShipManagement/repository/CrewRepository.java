package com.agile.shipmanagement.ShipManagement.repository;

import com.agile.shipmanagement.ShipManagement.model.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewRepository extends JpaRepository<Crew,Integer> {
    List<Crew> findAllByShip_ShipId(Integer shipId);
}
