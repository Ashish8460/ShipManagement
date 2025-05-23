package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.service.ShipService;
import com.agile.shipmanagement.ShipManagement.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/ships")
@Tag(name = "Ship Management", description = "Operations pertaining to Ship Management")
public class ShipController {


    @Autowired
    ShipService shipService;


    @PostMapping("/addShip")
    private ResponseEntity<?> addShip(@Valid @RequestBody Ship ship) {
        Ship ship1 = shipService.addShip(ship);
        return new ResponseEntity<>(ResponseUtil.buildResponse(200, "Ship Created Successfully.",
                ship1), HttpStatus.OK);
    }

    @GetMapping("/allShips")

    private ResponseEntity<?> getAllShips() {
        List<Ship> allShips = shipService.getAllShips();
        if (allShips.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Ships Found.",
                    new ArrayList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "All Ships Retrieved Successfully.",
                    shipService.getAllShips()), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Ship by ID", description = "Returns a ship object by its ID")
    private ResponseEntity<?> getShipDetailsById(@PathVariable("id") Integer id) {
        Ship shipById = shipService.getShipById(id);
        if (shipById == null) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Ship Found.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Ship Retrieved Successfully.",
                    shipService.getShipById(id)), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteShipById(@PathVariable("id") Integer id) {
        boolean isDeleted = shipService.deleteShipById(id);
        if (!isDeleted) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Ship Found.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Ship Deleted Successfully.",
                    new HashMap<>()), HttpStatus.OK);
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateShipById(@PathVariable("id") Integer id, @RequestBody Ship ship) {
        Ship updateShip = shipService.updateShip(id, ship);
        if (null == updateShip) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Ship Found.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Ship Updated Successfully.",
                    updateShip), HttpStatus.OK);
        }
    }

}
