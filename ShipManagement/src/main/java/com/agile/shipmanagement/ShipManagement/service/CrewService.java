package com.agile.shipmanagement.ShipManagement.service;

import com.agile.shipmanagement.ShipManagement.model.BaseEntityModel;
import com.agile.shipmanagement.ShipManagement.model.Crew;
import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.repository.CrewRepository;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CrewService {

    @Autowired
    CrewRepository crewRepository;

    @Autowired
    ShipRepository shipRepository;


    public Crew createAndAddCrewToShip(Integer shipId, Crew crew) {

        Ship ship = shipRepository.findById(shipId).orElseThrow(() -> new EntityNotFoundException("Ship not found"));
        crew.setShip(ship);
        crewRepository.save(crew);
        return crew;
    }


    public List<Crew> getCrewByShipId(Integer shipId) {
        shipRepository.findById(shipId).orElseThrow(() -> new EntityNotFoundException("Ship not found"));
        List<Crew> crewList = crewRepository.findAllByShip_ShipId(shipId);

        if (!crewList.isEmpty()) {
            return crewList;
        }
        return new ArrayList<>();
    }

    public Crew updateCrewPosition(Integer shipId, Integer crewId, Crew crew) {
        shipRepository.findById(shipId).orElseThrow(() -> new EntityNotFoundException("Ship not found"));

        Crew crewDeleted = crewRepository.findCrewByShip_ShipIdAndCrewId(shipId, crewId).orElseThrow(() -> new EntityNotFoundException("Crew not found"));
        crewRepository.delete(crewDeleted);
        return crewDeleted;
    }

    @Transactional
    public boolean deleteCrewFromShip(Integer shipId, Integer crewId) {
        boolean isDeleted = false;
        shipRepository.findById(shipId).orElseThrow(() -> new EntityNotFoundException("Ship not found"));

        Crew crewDeleted = crewRepository.findCrewByShip_ShipIdAndCrewId(shipId, crewId).orElseThrow(() -> new EntityNotFoundException("Crew not found"));
        crewRepository.delete(crewDeleted);
        isDeleted = true;
        return isDeleted;

    }
}
