package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.Crew;
import com.agile.shipmanagement.ShipManagement.service.CrewService;
import com.agile.shipmanagement.ShipManagement.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/ships")
public class CrewController {

    @Autowired
    CrewService crewService;


    @PostMapping("/{id}/crew")
    private ResponseEntity<?> addCrew(@PathVariable("id") Integer id, @Valid @RequestBody Crew crew) {
        Crew addCrewToShip = crewService.createAndAddCrewToShip(id,crew);
        if (null == addCrewToShip) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST.value(), "No Crew Added.",
                    new HashMap<>()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Added Successfully.",
                    addCrewToShip), HttpStatus.OK);
        }

    }

    @GetMapping("/{id}/crew")
    private ResponseEntity<?> getCrewByShipId(@PathVariable("id") Integer id) {
        List<Crew> creList = crewService.getCrewByShipId(id);
        if (creList.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST.value(), "No Crew Members Found.",
                    creList), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Members Fetched Successfully.",
                    creList), HttpStatus.OK);
        }

    }
}
