package com.agile.shipmanagement.ShipManagement.service;

import com.agile.shipmanagement.ShipManagement.model.Crew;
import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.repository.CrewRepository;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.persistence.EntityNotFoundException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CrewServiceTest {

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private CrewService crewService;

    private Ship testShip;
    private Crew testCrew;
    private Integer testShipId;
    private Integer testCrewId;

    @BeforeEach
    void setUp() {
        testShipId = 1;
        testCrewId = 1;

        testShip = new Ship();
        testShip.setShipId(testShipId);
        testShip.setName("Test Ship");

        testCrew = new Crew();
        testCrew.setCrewId(testCrewId);
        testCrew.setName("Test Crew");
        testCrew.setShip(testShip);
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testCreateAndAddCrewToShip_success() {
        // Arrange
        Integer shipId = 1;
        Ship mockShip = new Ship();
        mockShip.setShipId(shipId);
        mockShip.setName("Evergreen");

        Crew mockCrew = new Crew();
        mockCrew.setName("John");

        when(shipRepository.findById(shipId)).thenReturn(Optional.of(mockShip));
        when(crewRepository.save(any(Crew.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Crew savedCrew = crewService.createAndAddCrewToShip(shipId, mockCrew);

        // Assert
        assertNotNull(savedCrew);
        assertEquals("John", savedCrew.getName());
        assertEquals(mockShip, savedCrew.getShip());
        verify(shipRepository, times(1)).findById(shipId);
        verify(crewRepository, times(1)).save(mockCrew);
    }

    @Test
    void testCreateAndAddCrewToShip_shipNotFound() {
        // Arrange
        Integer shipId = 99;
        Crew crew = new Crew();

        when(shipRepository.findById(shipId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            crewService.createAndAddCrewToShip(shipId, crew);
        });

        assertEquals("Ship not found", exception.getMessage());
        verify(shipRepository, times(1)).findById(shipId);
        verifyNoInteractions(crewRepository);
    }

    @Test
    void getCrewByShipId_WithCrew() {
        // Arrange
        List<Crew> crewList = Arrays.asList(testCrew);
        when(shipRepository.findById(testShipId)).thenReturn(Optional.of(testShip));
        when(crewRepository.findAllByShip_ShipId(testShipId)).thenReturn(crewList);

        // Act
        List<Crew> result = crewService.getCrewByShipId(testShipId);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testCrew, result.get(0));

        verify(shipRepository).findById(testShipId);
        verify(crewRepository).findAllByShip_ShipId(testShipId);
    }

    @Test
    void getCrewByShipId_NoCrew() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.of(testShip));
        when(crewRepository.findAllByShip_ShipId(testShipId)).thenReturn(new ArrayList<>());

        // Act
        List<Crew> result = crewService.getCrewByShipId(testShipId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(shipRepository).findById(testShipId);
        verify(crewRepository).findAllByShip_ShipId(testShipId);
    }

    @Test
    void getCrewByShipId_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.getCrewByShipId(testShipId);
        });

        verify(shipRepository).findById(testShipId);
        verify(crewRepository, never()).findAllByShip_ShipId(any());
    }

    @Test
    void updateCrewPosition_Success() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.of(testShip));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId))
                .thenReturn(Optional.of(testCrew));
        doNothing().when(crewRepository).delete(any(Crew.class));

        // Act
        Crew result = crewService.updateCrewPosition(testShipId, testCrewId, testCrew);

        // Assert
        assertNotNull(result);
        assertEquals(testCrew, result);

        verify(shipRepository).findById(testShipId);
        verify(crewRepository).findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId);
        verify(crewRepository).delete(testCrew);
    }

    @Test
    void updateCrewPosition_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.updateCrewPosition(testShipId, testCrewId, testCrew);
        });

        verify(shipRepository).findById(testShipId);
        verify(crewRepository, never()).findCrewByShip_ShipIdAndCrewId(any(), any());
    }

    @Test
    void updateCrewPosition_CrewNotFound() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.of(testShip));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.updateCrewPosition(testShipId, testCrewId, testCrew);
        });

        verify(shipRepository).findById(testShipId);
        verify(crewRepository).findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId);
    }

    @Test
    void deleteCrewFromShip_Success() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.of(testShip));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId))
                .thenReturn(Optional.of(testCrew));
        doNothing().when(crewRepository).delete(any(Crew.class));

        // Act
        boolean result = crewService.deleteCrewFromShip(testShipId, testCrewId);

        // Assert
        assertTrue(result);

        verify(shipRepository).findById(testShipId);
        verify(crewRepository).findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId);
        verify(crewRepository).delete(testCrew);
    }

    @Test
    void deleteCrewFromShip_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.deleteCrewFromShip(testShipId, testCrewId);
        });

        verify(shipRepository).findById(testShipId);
        verify(crewRepository, never()).findCrewByShip_ShipIdAndCrewId(any(), any());
    }

    @Test
    void deleteCrewFromShip_CrewNotFound() {
        // Arrange
        when(shipRepository.findById(testShipId)).thenReturn(Optional.of(testShip));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.deleteCrewFromShip(testShipId, testCrewId);
        });

        verify(shipRepository).findById(testShipId);
        verify(crewRepository).findCrewByShip_ShipIdAndCrewId(testShipId, testCrewId);
    }
}