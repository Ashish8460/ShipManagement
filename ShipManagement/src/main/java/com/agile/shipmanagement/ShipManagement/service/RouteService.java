package com.agile.shipmanagement.ShipManagement.service;

import com.agile.shipmanagement.ShipManagement.model.PositionUpdateGroup;
import com.agile.shipmanagement.ShipManagement.model.Route;
import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.repository.RouteRepository;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ShipRepository shipRepository;


    public Route assignRouteToShip(Integer shipId,Route route) {
        Ship ship = shipRepository.findById(shipId).orElseThrow(() -> new IllegalStateException("Ship not found"));
        if (null != ship) {
            route.setShip(ship);
            routeRepository.save(route);
        }
        return route;
    }

    public List<Route> getRouteByShipId(Integer shipId) {
        Ship ship = shipRepository.findById(shipId).orElseThrow(() -> new IllegalStateException("Ship not found"));

        List<Route> allRoutesByShip = routeRepository.findAllByShip_ShipId(shipId).orElseThrow(() -> new IllegalStateException("Routes not found"));
        ;
        if (!allRoutesByShip.isEmpty()) {
            return routeRepository.findAllByShip_ShipId(shipId).orElseThrow(() -> new IllegalStateException("Routes not found"));
        }
        return new ArrayList<>();
    }

    public Route updateRoute(Integer shipId, Integer routeId, Route route) {
        Ship ship = shipRepository.findById(shipId).orElseThrow(() -> new IllegalStateException("Ship not found"));
        if (null != ship) {
            Route routeToBeUpdated = routeRepository.findById(routeId).orElseThrow(() -> new IllegalStateException("Route not found"));
            if (null != routeToBeUpdated) {
                // Only update fields that are not null in the request
                if (route.getPorts() != null) {
                    routeToBeUpdated.setPorts(route.getPorts());
                }
//                if (route.getDistance()) {
                    routeToBeUpdated.setDistance(route.getDistance());
//                }
                if (route.getArrivalTime() != null) {
                    routeToBeUpdated.setArrivalTime(route.getArrivalTime());
                }
                if (route.getDepartureTime() != null) {
                    routeToBeUpdated.setDepartureTime(route.getDepartureTime());
                }
//                if (route.getEstimatedTime() != null) {
                    routeToBeUpdated.setEstimatedTime(route.getEstimatedTime());
//                }
                routeRepository.save(routeToBeUpdated);
                return routeToBeUpdated;
            }
            return null;
        } else {
            return route;
        }
    }


    @Transactional
    public boolean deleteRouteById(Integer shipId, Integer routeId) {
        boolean isDeleted = false;
        Ship ship = shipRepository.findById(shipId).orElseThrow(() -> new IllegalStateException("Ship not found"));
        if (null != ship) {
            Route routeToBeDeleted = routeRepository.findById(routeId).orElseThrow(() -> new IllegalStateException("Route not found"));
            if (null != routeToBeDeleted) {
                routeRepository.delete(routeToBeDeleted);
                isDeleted = true;
                return isDeleted;
            }
            return  false;
        }else{
            return  false;
        }
    }
}
