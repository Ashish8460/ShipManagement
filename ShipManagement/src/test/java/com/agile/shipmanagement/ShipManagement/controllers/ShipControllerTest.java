package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.ResponseModel;
import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.service.CrewService;
import com.agile.shipmanagement.ShipManagement.service.ShipService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ShipControllerTest {

    @Mock
    private ShipService shipService;

    @InjectMocks
    private ShipController shipController;

    private Ship testShip;

    @BeforeEach
    void setUp() {
        testShip = new Ship();
        testShip.setShipId(1);
        testShip.setName("Test Ship");
        // Set other required properties
    }

    @Test
    void addShip_Success() {
        // Arrange
        when(shipService.addShip(any(Ship.class))).thenReturn(testShip);

        // Act
        ResponseEntity<?> response = shipController.addShip(testShip);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(200, responseBody.getStatusCode());
        assertEquals("Ship Created Successfully.", responseBody.getMessage());
        assertEquals(testShip, responseBody.getData());

        verify(shipService).addShip(any(Ship.class));
    }

    @Test
    void getAllShips_WithShips() {
        // Arrange
        List<Ship> ships = Arrays.asList(testShip);
        when(shipService.getAllShips()).thenReturn(ships);

        // Act
        ResponseEntity<?> response = shipController.getAllShips();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("All Ships Retrieved Successfully.", responseBody.getMessage());
        assertEquals(ships, responseBody.getData());

        verify(shipService, times(2)).getAllShips(); // Called twice due to implementation
    }

    @Test
    void getAllShips_NoShips() {
        // Arrange
        when(shipService.getAllShips()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<?> response = shipController.getAllShips();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Ships Found.", responseBody.getMessage());
        assertTrue(((List<?>) responseBody.getData()).isEmpty());

        verify(shipService).getAllShips();
    }

    @Test
    void getShipDetailsById_ExistingShip() {
        // Arrange
        when(shipService.getShipById(1)).thenReturn(testShip);

        // Act
        ResponseEntity<?> response = shipController.getShipDetailsById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("Ship Retrieved Successfully.", responseBody.getMessage());
        assertEquals(testShip, responseBody.getData());

        verify(shipService, times(2)).getShipById(1); // Called twice due to implementation
    }

    @Test
    void getShipDetailsById_NonExistingShip() {
        // Arrange
        when(shipService.getShipById(999)).thenReturn(null);

        // Act
        ResponseEntity<?> response = shipController.getShipDetailsById(999);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Ship Found.", responseBody.getMessage());
        assertTrue(((java.util.Map<?, ?>) responseBody.getData()).isEmpty());

        verify(shipService).getShipById(999);
    }

    @Test
    void deleteShipById_Success() {
        // Arrange
        when(shipService.deleteShipById(1)).thenReturn(true);

        // Act
        ResponseEntity<?> response = shipController.deleteShipById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("Ship Deleted Successfully.", responseBody.getMessage());
        assertTrue(((java.util.Map<?, ?>) responseBody.getData()).isEmpty());

        verify(shipService).deleteShipById(1);
    }

    @Test
    void deleteShipById_NotFound() {
        // Arrange
        when(shipService.deleteShipById(999)).thenReturn(false);

        // Act
        ResponseEntity<?> response = shipController.deleteShipById(999);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Ship Found.", responseBody.getMessage());
        assertTrue(((java.util.Map<?, ?>) responseBody.getData()).isEmpty());

        verify(shipService).deleteShipById(999);
    }

    @Test
    void updateShipById_Success() {
        // Arrange
        Ship updatedShip = new Ship();
        updatedShip.setShipId(1);
        updatedShip.setName("Updated Ship");
        when(shipService.updateShip(1, testShip)).thenReturn(updatedShip);

        // Act
        ResponseEntity<?> response = shipController.updateShipById(1, testShip);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("Ship Updated Successfully.", responseBody.getMessage());
        assertEquals(updatedShip, responseBody.getData());

        verify(shipService).updateShip(1, testShip);
    }

    @Test
    void updateShipById_NotFound() {
        // Arrange
        when(shipService.updateShip(999, testShip)).thenReturn(null);

        // Act
        ResponseEntity<?> response = shipController.updateShipById(999, testShip);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel<?> responseBody = (ResponseModel<?>) response.getBody();
        assertNotNull(responseBody);
        assertEquals(HttpStatus.OK.value(), responseBody.getStatusCode());
        assertEquals("No Ship Found.", responseBody.getMessage());
        assertTrue(((java.util.Map<?, ?>) responseBody.getData()).isEmpty());

        verify(shipService).updateShip(999, testShip);
    }
}