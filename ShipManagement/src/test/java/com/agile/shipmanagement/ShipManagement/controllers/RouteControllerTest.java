package com.agile.shipmanagement.ShipManagement.controllers;

import static org.junit.jupiter.api.Assertions.*;

import com.agile.shipmanagement.ShipManagement.model.ResponseModel;
import com.agile.shipmanagement.ShipManagement.model.Route;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import com.agile.shipmanagement.ShipManagement.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private RouteController routeController;

    private Route testRoute;
    private Integer testShipId;
    private Integer testRouteId;

    @BeforeEach
    void setUp() {
        testRoute = new Route();
        testRoute.setRouteId(1);
        testRoute.setPorts(List.of("Port A, Port B"));
        testShipId = 1;
        testRouteId = 1;
    }

    @Test
    void assignRouteToShip_Success() {
        // Arrange
        when(routeService.assignRouteToShip(testShipId, testRoute)).thenReturn(testRoute);

        // Act
        ResponseEntity<?> response = routeController.assignRouteToShip(testShipId, testRoute);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("Route Added Successfully.", responseBody.getMessage());
        assertEquals(testRoute, responseBody.getData());

        verify(routeService).assignRouteToShip(testShipId, testRoute);
    }

    @Test
    void assignRouteToShip_Failure() {
        // Arrange
        when(routeService.assignRouteToShip(testShipId, testRoute)).thenReturn(null);

        // Act
        ResponseEntity<?> response = routeController.assignRouteToShip(testShipId, testRoute);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Route Added.", responseBody.getMessage());
        assertTrue(((Map<?, ?>) responseBody.getData()).isEmpty());

        verify(routeService).assignRouteToShip(testShipId, testRoute);
    }

    @Test
    void getRouteByShipId_WithRoutes() {
        // Arrange
        List<Route> routes = Arrays.asList(testRoute);
        when(routeService.getRouteByShipId(testShipId)).thenReturn(routes);

        // Act
        ResponseEntity<?> response = routeController.getRouteByShipId(testShipId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("All Routes Retrieved Successfully.", responseBody.getMessage());
        assertEquals(routes, responseBody.getData());

        verify(routeService).getRouteByShipId(testShipId);
    }

    @Test
    void getRouteByShipId_NoRoutes() {
        // Arrange
        when(routeService.getRouteByShipId(testShipId)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = routeController.getRouteByShipId(testShipId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Routes Found.", responseBody.getMessage());
        assertTrue(((List<?>) responseBody.getData()).isEmpty());

        verify(routeService).getRouteByShipId(testShipId);
    }

    @Test
    void updateRoute_Success() {
        // Arrange
        Route updatedRoute = new Route();
        updatedRoute.setRouteId(testRouteId);
        updatedRoute.setPorts(List.of("Updated Port A"));
        when(routeService.updateRoute(testShipId, testRouteId, testRoute)).thenReturn(updatedRoute);

        // Act
        ResponseEntity<?> response = routeController.updateRoute(testShipId, testRouteId, testRoute);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("Route Updated Successfully.", responseBody.getMessage());
        assertEquals(updatedRoute, responseBody.getData());

        verify(routeService).updateRoute(testShipId, testRouteId, testRoute);
    }

    @Test
    void updateRoute_Failure() {
        // Arrange
        when(routeService.updateRoute(testShipId, testRouteId, testRoute)).thenReturn(null);

        // Act
        ResponseEntity<?> response = routeController.updateRoute(testShipId, testRouteId, testRoute);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Route Updated.", responseBody.getMessage());
        assertTrue(((Map<?, ?>) responseBody.getData()).isEmpty());

        verify(routeService).updateRoute(testShipId, testRouteId, testRoute);
    }

    @Test
    void deleteRouteForShip_Success() {
        // Arrange
        when(routeService.deleteRouteById(testShipId, testRouteId)).thenReturn(true);

        // Act
        ResponseEntity<?> response = routeController.deleteRouteForShip(testShipId, testRouteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("Route Deleted Successfully.", responseBody.getMessage());
        assertTrue(((Map<?, ?>) responseBody.getData()).isEmpty());

        verify(routeService).deleteRouteById(testShipId, testRouteId);
    }

    @Test
    void deleteRouteForShip_Failure() {
        // Arrange
        when(routeService.deleteRouteById(testShipId, testRouteId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = routeController.deleteRouteForShip(testShipId, testRouteId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Route Deleted.", responseBody.getMessage());
        assertTrue(((Map<?, ?>) responseBody.getData()).isEmpty());

        verify(routeService).deleteRouteById(testShipId, testRouteId);
    }
}