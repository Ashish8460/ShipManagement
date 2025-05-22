package com.agile.shipmanagement.ShipManagement.repository;

import com.agile.shipmanagement.ShipManagement.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Integer> {
    List<Ship> findAllByOrderByShipIdAsc();
    Optional<Ship> findByShipId(Integer shipId);
    Ship deleteShipsByShipId(Integer shipId);
}
