package com.agile.shipmanagement.ShipManagement.enums;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ShipStatusTest {

    @Test
    void getValue_ShouldReturnCorrectStringValue() {
        assertEquals("Active", ShipStatus.ACTIVE.getValue());
        assertEquals("Under Maintenance", ShipStatus.UNDERMAINTENANCE.getValue());
    }

    @Test
    void fromValue_ShouldCreateCorrectEnum_WhenValidValueProvided() {
        assertEquals(ShipStatus.ACTIVE, ShipStatus.fromValue("Active"));
        assertEquals(ShipStatus.UNDERMAINTENANCE, ShipStatus.fromValue("Under Maintenance"));
    }

    @Test
    void fromValue_ShouldBeCaseInsensitive() {
        assertEquals(ShipStatus.ACTIVE, ShipStatus.fromValue("active"));
        assertEquals(ShipStatus.ACTIVE, ShipStatus.fromValue("ACTIVE"));
        assertEquals(ShipStatus.UNDERMAINTENANCE, ShipStatus.fromValue("under maintenance"));
        assertEquals(ShipStatus.UNDERMAINTENANCE, ShipStatus.fromValue("UNDER MAINTENANCE"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void fromValue_ShouldThrowException_WhenNullOrEmptyOrBlank(String input) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ShipStatus.fromValue(input)
        );
        assertEquals("ShipStatus value cannot be empty or null", exception.getMessage());
    }

    @Test
    void fromValue_ShouldThrowException_WhenInvalidValueProvided() {
        String invalidValue = "Invalid Status";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ShipStatus.fromValue(invalidValue)
        );
        assertEquals("Unknown ShipStatus value: " + invalidValue, exception.getMessage());
    }
}