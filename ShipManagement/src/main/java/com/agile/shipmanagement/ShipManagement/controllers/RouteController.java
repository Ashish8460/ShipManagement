package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.PositionUpdateGroup;
import com.agile.shipmanagement.ShipManagement.model.Route;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import com.agile.shipmanagement.ShipManagement.service.RouteService;
import com.agile.shipmanagement.ShipManagement.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/ships")
@Tag(name = "Route Management", description = "Operations pertaining to Route Management")
public class RouteController {

    @Autowired
    RouteService routeService;

    @Autowired
    ShipRepository shipRepository;

    @PostMapping("/{id}/routes")
    private ResponseEntity<?> assignRouteToShip(@PathVariable("id") Integer shipId, @Validated @RequestBody Route route) {
        Route routeAdded = routeService.assignRouteToShip(shipId, route);
        if (null == routeAdded) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Route Added.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Route Added Successfully.",
                    routeAdded), HttpStatus.OK);
        }
    }


    @GetMapping("/{id}/routes")
    private ResponseEntity<?> getRouteByShipId(@PathVariable("id") Integer shipId) {
        List<Route> routeList = routeService.getRouteByShipId(shipId);
        if (routeList.isEmpty()) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Routes Found.",
                    new ArrayList()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "All Routes Retrieved Successfully.",
                    routeList), HttpStatus.OK);
        }

    }

    @PutMapping("/{id}/routes/{routeId}")
    private ResponseEntity<?> updateRoute(@PathVariable("id") Integer shipId, @PathVariable("routeId") Integer routeId, @Valid @RequestBody Route route) {

        Route routeUpdated = routeService.updateRoute(shipId, routeId, route);
        if (null == routeUpdated) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Route Updated.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Route Updated Successfully.",
                    routeUpdated), HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}/routes/{routeId}")
    private ResponseEntity<?> deleteRouteForShip(@PathVariable("id") Integer shipId, @PathVariable("routeId") Integer routeId) {
        boolean isDeleted = routeService.deleteRouteById(shipId, routeId);
        if (!isDeleted) {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "No Route Deleted.",
                    new HashMap<>()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseUtil.buildResponse(HttpStatus.OK.value(), "Route Deleted Successfully.",
                    new HashMap<>()), HttpStatus.OK);
        }

    }
}
