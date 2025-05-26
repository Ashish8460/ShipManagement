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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShipServiceTest {

    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private ShipService shipService;

    private Ship testShip;

    @BeforeEach
    void setUp() {
        testShip = new Ship();
        testShip.setShipId(1);
        testShip.setName("Test Ship");
        testShip.setCapacity(1000.0);
        testShip.setType(ShipType.CARGO);
        testShip.setStatus(ShipStatus.ACTIVE);
    }

    @Test
    void addShip_Success() {
        // Arrange
        when(shipRepository.save(any(Ship.class))).thenReturn(testShip);

        // Act
        Ship savedShip = shipService.addShip(testShip);

        // Assert
        assertNotNull(savedShip);
        assertEquals(testShip.getName(), savedShip.getName());
        assertEquals(testShip.getCapacity(), savedShip.getCapacity());
        assertEquals(testShip.getType(), savedShip.getType());
        assertEquals(testShip.getStatus(), savedShip.getStatus());

        verify(shipRepository).save(any(Ship.class));
    }

    @Test
    void getAllShips_WithShips() {
        // Arrange
        List<Ship> ships = Arrays.asList(testShip);
        when(shipRepository.findAllByOrderByShipIdAsc()).thenReturn(ships);

        // Act
        List<Ship> result = shipService.getAllShips();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testShip, result.get(0));

        verify(shipRepository).findAllByOrderByShipIdAsc();
    }

    @Test
    void getAllShips_NoShips() {
        // Arrange
        when(shipRepository.findAllByOrderByShipIdAsc()).thenReturn(Arrays.asList());

        // Act
        List<Ship> result = shipService.getAllShips();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(shipRepository).findAllByOrderByShipIdAsc();
    }

    @Test
    void getShipById_Success() {
        // Arrange
        when(shipRepository.findByShipId(1)).thenReturn(Optional.of(testShip));

        // Act
        Ship result = shipService.getShipById(1);

        // Assert
        assertNotNull(result);
        assertEquals(testShip.getShipId(), result.getShipId());
        assertEquals(testShip.getName(), result.getName());

        verify(shipRepository).findByShipId(1);
    }

    @Test
    void getShipById_NotFound() {
        // Arrange
        when(shipRepository.findByShipId(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            shipService.getShipById(999);
        });

        verify(shipRepository).findByShipId(999);
    }

    @Test
    void deleteShipById_Success() {
        // Arrange
        when(shipRepository.findById(1)).thenReturn(Optional.of(testShip));
        doNothing().when(shipRepository).delete(any(Ship.class));

        // Act
        boolean result = shipService.deleteShipById(1);

        // Assert
        assertTrue(result);
        verify(shipRepository).findById(1);
        verify(shipRepository).delete(testShip);
    }

    @Test
    void deleteShipById_NotFound() {
        // Arrange
        when(shipRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            shipService.deleteShipById(999);
        });

        verify(shipRepository).findById(999);
        verify(shipRepository, never()).delete(any(Ship.class));
    }

    @Test
    void updateShip_Success() {
        // Arrange
        Ship updatedShip = new Ship();
        updatedShip.setName("Updated Ship");
        updatedShip.setCapacity(2000.0);
        updatedShip.setType(ShipType.CRUISE);
        updatedShip.setStatus(ShipStatus.ACTIVE);

        when(shipRepository.findById(1)).thenReturn(Optional.of(testShip));
        when(shipRepository.save(any(Ship.class))).thenReturn(testShip);

        // Act
        Ship result = shipService.updateShip(1, updatedShip);

        // Assert
        assertNotNull(result);
        assertEquals(updatedShip.getName(), result.getName());
        assertEquals(updatedShip.getCapacity(), result.getCapacity());
        assertEquals(updatedShip.getType(), result.getType());
        assertEquals(updatedShip.getStatus(), result.getStatus());

        verify(shipRepository).findById(1);
        verify(shipRepository).save(any(Ship.class));
    }

    @Test
    void updateShip_NotFound() {
        // Arrange
        when(shipRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            shipService.updateShip(999, testShip);
        });

        verify(shipRepository).findById(999);
        verify(shipRepository, never()).save(any(Ship.class));
    }

    @Test
    void updateShip_WithNullValues() {
        // Arrange
        Ship shipWithNullValues = new Ship();
        when(shipRepository.findById(1)).thenReturn(Optional.of(testShip));
        when(shipRepository.save(any(Ship.class))).thenReturn(testShip);

        // Act
        Ship result = shipService.updateShip(1, shipWithNullValues);

        // Assert
        assertNotNull(result);
        assertNull(result.getName());
        assertEquals(0.0, result.getCapacity());
        assertNull(result.getType());
        assertNull(result.getStatus());

        verify(shipRepository).findById(1);
        verify(shipRepository).save(any(Ship.class));
    }
}