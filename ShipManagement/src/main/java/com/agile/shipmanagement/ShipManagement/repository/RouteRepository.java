package com.agile.shipmanagement.ShipManagement.repository;

import com.agile.shipmanagement.ShipManagement.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    Optional<List<Route>> findAllByShip_ShipId(int shipId);

}
