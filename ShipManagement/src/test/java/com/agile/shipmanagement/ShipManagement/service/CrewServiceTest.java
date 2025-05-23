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

class CrewServiceTest {

    @Mock
    private CrewRepository crewRepository;

    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private CrewService crewService;

    private Ship ship;
    private Crew crew;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        ship = new Ship();
        ship.setShipId(1);
        
        crew = new Crew();
        crew.setCrewId(1);
    }

    @Test
    void createAndAddCrewToShip_Success() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.save(any(Crew.class))).thenReturn(crew);

        // Act
        Crew result = crewService.createAndAddCrewToShip(1, crew);

        // Assert
        assertNotNull(result);
        assertEquals(ship, result.getShip());
        verify(crewRepository, times(1)).save(crew);
    }

    @Test
    void createAndAddCrewToShip_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.createAndAddCrewToShip(1, crew);
        });
        verify(crewRepository, never()).save(any(Crew.class));
    }

    @Test
    void getCrewByShipId_Success() {
        // Arrange
        List<Crew> crewList = Arrays.asList(new Crew(), new Crew());
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.findAllByShip_ShipId(1)).thenReturn(crewList);

        // Act
        List<Crew> result = crewService.getCrewByShipId(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(crewRepository, times(1)).findAllByShip_ShipId(1);
    }

    @Test
    void getCrewByShipId_EmptyList() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.findAllByShip_ShipId(1)).thenReturn(new ArrayList<>());

        // Act
        List<Crew> result = crewService.getCrewByShipId(1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCrewByShipId_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.getCrewByShipId(1);
        });
    }

    @Test
    void updateCrewPosition_Success() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(1, 1)).thenReturn(Optional.of(crew));

        if(null != crew){
            // Act
            Crew result = crewService.updateCrewPosition(1, 1, crew);

            // Assert
            assertNotNull(result);
            verify(crewRepository, times(1)).delete(crew);
        }



        // Case 2: Crew not found - exception thrown before null check
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(1, 1))
                .thenReturn(Optional.empty());

        // Act & Assert - exception case
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.updateCrewPosition(1, 1, crew);
        });
        // Verify delete was not called in exception case
        verify(crewRepository, times(1)).delete(any());

    }

    @Test
    void updateCrewPosition_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.updateCrewPosition(1, 1, crew);
        });
        verify(crewRepository, never()).delete(any(Crew.class));
    }

    @Test
    void updateCrewPosition_CrewNotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(1, 1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.updateCrewPosition(1, 1, crew);
        });
        verify(crewRepository, never()).delete(any(Crew.class));
    }

    @Test
    void deleteCrewFromShip_Success() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(1, 1)).thenReturn(Optional.of(crew));

        // Act
        boolean result = crewService.deleteCrewFromShip(1, 1);

        // Assert
        assertTrue(result);
        verify(crewRepository, times(1)).delete(crew);
    }

    @Test
    void deleteCrewFromShip_ShipNotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.deleteCrewFromShip(1, 1);
        });
        verify(crewRepository, never()).delete(any(Crew.class));
    }

    @Test
    void deleteCrewFromShip_CrewNotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));
        when(crewRepository.findCrewByShip_ShipIdAndCrewId(1, 1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            crewService.deleteCrewFromShip(1, 1);
        });
        verify(crewRepository, never()).delete(any(Crew.class));
    }
}