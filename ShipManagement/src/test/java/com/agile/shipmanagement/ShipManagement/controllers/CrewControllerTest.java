package com.agile.shipmanagement.ShipManagement.controllers;

import com.agile.shipmanagement.ShipManagement.model.Crew;
import com.agile.shipmanagement.ShipManagement.model.ResponseModel;
import com.agile.shipmanagement.ShipManagement.service.CrewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
class CrewControllerTest {

    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCrew_Success() {
        // Arrange
        Integer shipId = 1;
        Crew crew = new Crew();
        Crew savedCrew = new Crew();
        when(crewService.createAndAddCrewToShip(eq(shipId), any(Crew.class))).thenReturn(savedCrew);

        // Act
        ResponseEntity<?> response = crewController.addCrew(shipId, crew);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("Crew Added Successfully.", responseModel.getMessage());
        assertEquals(savedCrew, responseModel.getData());
    }

    @Test
    void addCrew_Failure() {
        // Arrange
        Integer shipId = 1;
        Crew crew = new Crew();
        when(crewService.createAndAddCrewToShip(eq(shipId), any(Crew.class))).thenReturn(null);

        // Act
        ResponseEntity<?> response = crewController.addCrew(shipId, crew);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("No Crew Added.", responseModel.getMessage());
    }

    @Test
    void getCrewByShipId_Success() {
        // Arrange
        Integer shipId = 1;
        List<Crew> crewList = List.of(new Crew(), new Crew());
        when(crewService.getCrewByShipId(shipId)).thenReturn(crewList);

        // Act
        ResponseEntity<?> response = crewController.getCrewByShipId(shipId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("Crew Members Fetched Successfully.", responseModel.getMessage());
        assertEquals(crewList, responseModel.getData());
    }

    @Test
    void getCrewByShipId_EmptyList() {
        // Arrange
        Integer shipId = 1;
        List<Crew> emptyList = new ArrayList<>();
        when(crewService.getCrewByShipId(shipId)).thenReturn(emptyList);

        // Act
        ResponseEntity<?> response = crewController.getCrewByShipId(shipId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("No Crew Members Found.", responseModel.getMessage());
        assertEquals(emptyList, responseModel.getData());
    }

    @Test
    void updateCrewPosition_Success() {
        // Arrange
        Integer shipId = 1;
        Integer crewId = 1;
        Crew crew = new Crew();
        Crew updatedCrew = new Crew();
        when(crewService.updateCrewPosition(eq(shipId), eq(crewId), any(Crew.class))).thenReturn(updatedCrew);

        // Act
        ResponseEntity<?> response = crewController.updateCrewPosition(shipId, crewId, crew);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("Crew Updated Successfully.", responseModel.getMessage());
        assertEquals(updatedCrew, responseModel.getData());
    }

    @Test
    void updateCrewPosition_Failure() {
        // Arrange
        Integer shipId = 1;
        Integer crewId = 1;
        Crew crew = new Crew();
        when(crewService.updateCrewPosition(eq(shipId), eq(crewId), any(Crew.class))).thenReturn(null);

        // Act
        ResponseEntity<?> response = crewController.updateCrewPosition(shipId, crewId, crew);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("No Crew Updated.", responseModel.getMessage());
    }

    @Test
    void deleteCrewFromShip_Success() {
        // Arrange
        Integer shipId = 1;
        Integer crewId = 1;
        when(crewService.deleteCrewFromShip(shipId, crewId)).thenReturn(true);

        // Act
        ResponseEntity<?> response = crewController.deleteCrewFromShip(shipId, crewId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("Crew Deleted Successfully.", responseModel.getMessage());
    }

    @Test
    void deleteCrewFromShip_Failure() {
        // Arrange
        Integer shipId = 1;
        Integer crewId = 1;
        when(crewService.deleteCrewFromShip(shipId, crewId)).thenReturn(false);

        // Act
        ResponseEntity<?> response = crewController.deleteCrewFromShip(shipId, crewId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseModel responseModel = (ResponseModel) response.getBody();
        assertNotNull(responseModel);
        assertEquals(HttpStatus.OK.value(), responseModel.getStatusCode());
        assertEquals("No Crew Deleted.", responseModel.getMessage());
    }
}