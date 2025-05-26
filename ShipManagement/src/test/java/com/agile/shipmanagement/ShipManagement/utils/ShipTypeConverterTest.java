package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.enums.ShipType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTypeConverterTest {

    private final ShipTypeConverter converter = new ShipTypeConverter();

    @Test
    void convertToDatabaseColumnActive() {
        String result = converter.convertToDatabaseColumn(ShipType.CARGO);
        assertNotNull(result);
    }
    @Test
    void convertToDatabaseColumnNull() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }

    @Test
    void convertToEntityAttribute() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute("Military");
        });
        assertTrue(exception.getMessage().contains("Unknown ShipType value: Military"));
    }


}