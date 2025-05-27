package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.Crew;
import com.agile.shipmanagement.ShipManagement.model.PositionUpdateGroup;
import com.agile.shipmanagement.ShipManagement.service.CrewService;
import com.agile.shipmanagement.ShipManagement.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/ships")
@Tag(name = "Crew Management", description = "Operations pertaining to Crew Management")
public class CrewController {

    @Autowired
    CrewService crewService;


    @PostMapping("/{id}/crew")
    public ResponseEntity<?> addCrew(@PathVariable("id") Integer id, @Valid @RequestBody Crew crew) {
        Crew addCrewToShip = crewService.createAndAddCrewToShip(id, crew);
        if (null == addCrewToShip) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Crew Added.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Added Successfully.",
                    addCrewToShip), HttpStatus.OK);
        }

    }

    @GetMapping("/{id}/crew")
    public ResponseEntity<?> getCrewByShipId(@PathVariable("id") Integer id) {
        List<Crew> creList = crewService.getCrewByShipId(id);
        if (creList.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Crew Members Found.",
                    creList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Members Fetched Successfully.",
                    creList), HttpStatus.OK);
        }
    }


    @PutMapping("/{id}/crew/{crewId}")
    public ResponseEntity<?> updateCrewPosition(@PathVariable("id") Integer id, @PathVariable("crewId") Integer crewId, @Validated(PositionUpdateGroup.class) @RequestBody Crew crew) {
        Crew updateCrewObj = crewService.updateCrewPosition(id, crewId, crew);
        if (null == updateCrewObj) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Crew Updated.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Updated Successfully.",
                    updateCrewObj), HttpStatus.OK);
        }

    }


    @DeleteMapping("/{id}/crew/{crewId}")
    public ResponseEntity<?> deleteCrewFromShip(@PathVariable("id") Integer id, @PathVariable("crewId") Integer crewId) {
        boolean isDeleted = crewService.deleteCrewFromShip(id, crewId);
        if (!isDeleted) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Crew Deleted.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Deleted Successfully.",
                    new HashMap<>()), HttpStatus.OK);
        }

    }


    @GetMapping("/crew/{name}")
    ResponseEntity<?> getCrewByName(@PathVariable("name") String name) {
        Crew crewByName = crewService.getCrewByName(name);
        if (null != crewByName) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Crew Found Successfully.",
                    crewByName), HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Crew Found.",
                new HashMap<>()), HttpStatus.OK);
    }
}
