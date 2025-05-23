package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.Route;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import com.agile.shipmanagement.ShipManagement.service.RouteService;
import com.agile.shipmanagement.ShipManagement.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/ships")
public class RouteController {

    @Autowired
    RouteService routeService;

    @Autowired
    ShipRepository shipRepository;

    @PostMapping("/{id}/routes")
    private ResponseEntity<?> assignRouteToShip(@PathVariable("id") Integer shipId,@Validated @RequestBody Route route) {
        Route routeAdded = routeService.assignRouteToShip(shipId, route);
        if (null == routeAdded) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST.value(), "No Route Added.",
                    new HashMap<>()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Route Added Successfully.",
                    routeAdded), HttpStatus.OK);
        }
    }


    @GetMapping("/{id}/routes")
    private ResponseEntity<?> getRouteByShipId(@PathVariable("id") Integer shipId) {
        return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "All Routes Retrieved Successfully.",
                routeService.getRouteByShipId(shipId)), HttpStatus.OK);
    }

    @PutMapping("/{id}/routes")
    private ResponseEntity<?> updateRoute(@PathVariable("id") Integer shipId, @PathVariable("routeId") Integer routeId, Route route) {
        Route routeUpdated = routeService.updateRoute(shipId, routeId, route);
        if (null == routeUpdated) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST.value(), "No Route Updated.",
                    new HashMap<>()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Route Updated Successfully.",
                    routeUpdated), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}/routes/{routeId}")
    private ResponseEntity<?> deleteRouteForShip(@PathVariable("id") Integer shipId, @PathVariable("routeId") Integer routeId) {
        boolean isDeleted = routeService.deleteRouteById(shipId, routeId);
        if (!isDeleted) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST.value(), "No Route Deleted.",
                    new HashMap<>()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Route Deleted Successfully.",
                    new HashMap<>()), HttpStatus.OK);
        }

    }
}
