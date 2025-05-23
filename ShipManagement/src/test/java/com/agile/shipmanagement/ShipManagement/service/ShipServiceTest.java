package com.agile.shipmanagement.ShipManagement.service;

import com.agile.shipmanagement.ShipManagement.enums.ShipStatus;
import com.agile.shipmanagement.ShipManagement.enums.ShipType;
import com.agile.shipmanagement.ShipManagement.model.Crew;
import com.agile.shipmanagement.ShipManagement.model.Ship;
import com.agile.shipmanagement.ShipManagement.repository.ShipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShipServiceTest {

    @Mock
    private ShipRepository shipRepository;


    @InjectMocks
    private ShipService shipService;

    private Ship ship;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ship = new Ship();
        ship.setShipId(1);
        ship.setName("Cruiser Antarctica");
        ship.setType(ShipType.CRUISE);
        ship.setStatus(ShipStatus.ACTIVE);
        ship.setCapacity(1000);
    }

    @Test
    void addShip() {

        when(shipRepository.save(ship)).thenReturn(ship);

        Ship savedShip = shipService.addShip(ship);

        assertNotNull(savedShip);
        assertEquals(ship, savedShip);
        verify(shipRepository, times(1)).save(ship);

    }

    @Test
    void getAllShips() {
        when(shipRepository.findAllByOrderByShipIdAsc()).thenReturn(List.of(ship));

        List<Ship> ships = shipService.getAllShips();

        assertNotNull(ships);
        assertEquals(1, ships.size());
        assertEquals(ship, ships.get(0));
        //Check that the mock shipRepository had its method findAllByOrderByShipIdAsc() called exactly 1 time during the test execution.
        verify(shipRepository, times(1)).findAllByOrderByShipIdAsc();
        verifyNoMoreInteractions(shipRepository); //  Mockito is used to verify that no other interactions happened on the given mock(s) beyond what you've explicitly verified.
    }

    @Test
    void getAllShips_EmptyList() {

        when(shipRepository.findAllByOrderByShipIdAsc()).thenReturn(List.of());
        List<Ship> ships = shipService.getAllShips();
        assertNotNull(ships);
        assertTrue(ships.isEmpty());
        verify(shipRepository, times(1)).findAllByOrderByShipIdAsc();
        verifyNoMoreInteractions(shipRepository);
    }


    @Test
    void getShipById_Success() {
        // Arrange
        when(shipRepository.findByShipId(1)).thenReturn(Optional.ofNullable(ship));

        Ship shipById = shipService.getShipById(1);
        assertNotNull(shipById);
        assertEquals(ship, shipById);
        verify(shipRepository, times(1)).findByShipId(1);
        verifyNoMoreInteractions(shipRepository);
    }

    @Test
    void getShipById_Empty() {
        // Arrange
        when(shipRepository.findByShipId(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            shipService.getShipById(1);
        });
    }

    @Test
    void deleteShipById_Success() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(ship));

        // Act
        boolean isDeleted = shipService.deleteShipById(1);

        // Assert
        assertTrue(isDeleted);
        verify(shipRepository, times(1)).findById(1);
        verify(shipRepository, times(1)).delete(ship);
        verifyNoMoreInteractions(shipRepository);
    }

    @Test
    void deleteShipById_NotFound() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            shipService.deleteShipById(1);
        });
        verify(shipRepository, times(1)).findById(1);
        verifyNoMoreInteractions(shipRepository);
    }

    @Test
    void updateShip_Success() {
        // Arrange
        Ship existingShip = new Ship();
        existingShip.setShipId(1);
        existingShip.setName("Old Name");
        existingShip.setType(ShipType.CARGO);
        existingShip.setStatus(ShipStatus.ACTIVE);
        existingShip.setCapacity(1000);

        Ship updatedShip = new Ship();
        updatedShip.setName("New Name");
        updatedShip.setType(ShipType.CRUISE);
        updatedShip.setStatus(ShipStatus.UNDERMAINTENANCE);
        updatedShip.setCapacity(2000);

        when(shipRepository.findById(1)).thenReturn(Optional.of(existingShip));
        when(shipRepository.save(any(Ship.class))).thenReturn(existingShip);

        // Act
        Ship result = shipService.updateShip(1, updatedShip);

        // Assert
        assertNotNull(result);
        assertEquals(updatedShip.getName(), result.getName());
        assertEquals(updatedShip.getType(), result.getType());
        assertEquals(updatedShip.getStatus(), result.getStatus());
        assertEquals(updatedShip.getCapacity(), result.getCapacity());

        verify(shipRepository).findById(1);
        verify(shipRepository).save(existingShip);
        verifyNoMoreInteractions(shipRepository);
    }


    @Test
    void updateShip_NotFound() {
        // Arrange
        Ship updatedShip = new Ship();
        updatedShip.setName("New Name");
        updatedShip.setType(ShipType.CRUISE);
        updatedShip.setStatus(ShipStatus.UNDERMAINTENANCE);
        updatedShip.setCapacity(2000);

        when(shipRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            shipService.updateShip(1, updatedShip);
        });

        verify(shipRepository).findById(1);
        verifyNoMoreInteractions(shipRepository);
    }

    @Test
    void updateShip_WithNullValues() {
        // Arrange
        Ship existingShip = new Ship();
        existingShip.setShipId(1);
        existingShip.setName("Old Name");
        existingShip.setType(ShipType.CARGO);
        existingShip.setStatus(ShipStatus.ACTIVE);
        existingShip.setCapacity(1000);

        Ship updatedShip = new Ship();
        // All fields are null

        when(shipRepository.findById(1)).thenReturn(Optional.of(existingShip));
        when(shipRepository.save(any(Ship.class))).thenReturn(existingShip);

        // Act
        Ship result = shipService.updateShip(1, updatedShip);

        // Assert
        assertNotNull(result);
        // Verify that original values are preserved when update values are null
        assertEquals(existingShip.getName(), result.getName());
        assertEquals(existingShip.getType(), result.getType());
        assertEquals(existingShip.getStatus(), result.getStatus());
        assertEquals(existingShip.getCapacity(), result.getCapacity());

        verify(shipRepository).findById(1);
        verify(shipRepository).save(existingShip);
        verifyNoMoreInteractions(shipRepository);
    }

}