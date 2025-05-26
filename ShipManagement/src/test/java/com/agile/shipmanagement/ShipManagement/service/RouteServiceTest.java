package com.agile.shipmanagement.ShipManagement.service;

import com.agile.shipmanagement.ShipManagement.model.Route;
import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.repository.RouteRepository;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private RouteService routeService;

    private Ship testShip;
    private Route testRoute;
    private Integer shipId;
    private Integer routeId;

    @BeforeEach
    void setUp() {
        shipId = 1;
        routeId = 1;
        testShip = new Ship();
        testShip.setShipId(shipId);

        testRoute = new Route();
        testRoute.setRouteId(routeId);
        testRoute.setPorts(List.of("Port A, Port B"));
        testRoute.setDistance(100.0);
        testRoute.setArrivalTime(LocalDateTime.now().plusDays(1));
        testRoute.setDepartureTime(LocalDateTime.now());
        testRoute.setEstimatedTime(24.0);
    }

    @Test
    void assignRouteToShip_Success() {
        when(shipRepository.findById(shipId)).thenReturn(Optional.of(testShip));
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);

        Route result = routeService.assignRouteToShip(shipId, testRoute);

        assertNotNull(result);
        assertEquals(testShip, result.getShip());
        verify(routeRepository).save(testRoute);
    }

    @Test
    void assignRouteToShip_ShipNotFound() {
        when(shipRepository.findById(shipId)).thenThrow(new IllegalStateException("Ship not found"));

        assertThrows(IllegalStateException.class, () -> routeService.assignRouteToShip(shipId, testRoute));
        verify(routeRepository, never()).save(any(Route.class));
    }

    @Test
    void getRouteByShipId_Success() {
        List<Route> routes = Arrays.asList(testRoute);
        when(shipRepository.findById(shipId)).thenReturn(Optional.of(testShip));
        when(routeRepository.findAllByShip_ShipId(shipId)).thenReturn(Optional.of(routes));

        List<Route> result = routeService.getRouteByShipId(shipId);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testRoute, result.get(0));
    }

    @Test
    void getRouteByShipId_NoRoutes() {
        when(shipRepository.findById(shipId)).thenReturn(Optional.of(testShip));
        when(routeRepository.findAllByShip_ShipId(shipId)).thenThrow(new IllegalStateException("Routes not found"));

        assertThrows(IllegalStateException.class, () -> routeService.getRouteByShipId(shipId));
    }

    @Test
    void updateRoute_Success() {
        Route updateRoute = new Route();
        updateRoute.setPorts(List.of("Miami", "Banglore"));
        updateRoute.setDistance(200.0);

        when(shipRepository.findById(shipId)).thenReturn(Optional.of(testShip));
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(testRoute));
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);

        Route result = routeService.updateRoute(shipId, routeId, updateRoute);

        assertNotNull(result);
        assertEquals(List.of("Miami", "Banglore"), result.getPorts());
        assertEquals(200.0, result.getDistance());
        verify(routeRepository).save(any(Route.class));
    }

    @Test
    void updateRoute_ShipNotFound() {
        when(shipRepository.findById(shipId)).thenThrow(new IllegalStateException("Ship not found"));

        assertThrows(IllegalStateException.class, () ->
                routeService.updateRoute(shipId, routeId, testRoute));
    }

    @Test
    void deleteRouteById_Success() {
        when(shipRepository.findById(shipId)).thenReturn(Optional.of(testShip));
        when(routeRepository.findById(routeId)).thenReturn(Optional.of(testRoute));

        boolean result = routeService.deleteRouteById(shipId, routeId);

        assertTrue(result);
        verify(routeRepository).delete(testRoute);
    }

    @Test
    void deleteRouteById_RouteNotFound() {
        when(shipRepository.findById(shipId)).thenReturn(Optional.of(testShip));
        when(routeRepository.findById(routeId)).thenThrow(new IllegalStateException("Route not found"));

        assertThrows(IllegalStateException.class, () ->
                routeService.deleteRouteById(shipId, routeId));
        verify(routeRepository, never()).delete(any(Route.class));
    }
}