package com.agile.shipmanagement.ShipManagement.service;

import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ShipService {

    @Autowired
    ShipRepository shipRepository;


    public Ship addShip(Ship ship) {

        return shipRepository.save(ship);
    }


    public List<Ship> getAllShips() {
        return shipRepository.findAllByOrderByShipIdAsc();
    }


    public Ship getShipById(Integer shipId) {
        return shipRepository.findByShipId(shipId).orElseThrow(() -> new EntityNotFoundException("Ship not found"));
    }

    @Transactional
    public boolean deleteShipById(Integer id) {
        boolean isDeleted = false;
        Ship ship = shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ship not found"));

        shipRepository.delete(ship);
        isDeleted = true;
        return isDeleted;
    }


    @Transactional
    public Ship updateShip(Integer id, Ship shipToBeUpdated) {
        Ship ship = shipRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ship not found"));

        ship.setCapacity(shipToBeUpdated.getCapacity());
        ship.setName(shipToBeUpdated.getName());
        ship.setStatus(shipToBeUpdated.getStatus());
        ship.setCapacity(shipToBeUpdated.getCapacity());
        ship.setType(shipToBeUpdated.getType());

        shipRepository.save(ship);
        return ship;
    }


}
