package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.enums.ShipStatus;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ShipStatusConverterTest {

    private final ShipStatusConverter converter = new ShipStatusConverter();

    @Test
    void testConvertActive() {
        String result = converter.convertToDatabaseColumn(ShipStatus.ACTIVE);
        assertEquals("Active", result);
    }


    @Test
    void testConvertUnderMaintenance() {
        String result = converter.convertToDatabaseColumn(ShipStatus.UNDERMAINTENANCE);
        assertEquals("Under Maintenance", result);
    }

    @Test
    void testConvertNull() {
        String result = converter.convertToDatabaseColumn(null);
        assertNull(result);
    }


    @Test
    void testConvertUnknownStatus() {
        // You can simulate an unknown value with a mock if needed, but not with enums directly
        // So this case is only valid if your enum has future values, otherwise it's unreachable
        assertThrows(IllegalArgumentException.class, () -> {
            ShipStatus invalidStatus = null;
            for (ShipStatus status : ShipStatus.values()) {
                if (!status.equals(ShipStatus.ACTIVE) && !status.equals(ShipStatus.UNDERMAINTENANCE)) {
                    invalidStatus = status;
                }
            }

            // simulate unknown by casting
            converter.convertToDatabaseColumn(Objects.requireNonNullElseGet(invalidStatus, () -> (ShipStatus) Enum.valueOf((Class) Enum.class, "UNKNOWN")));
        });
    }


    @Test
    void testConvertToEntityAttribute_Active() {
        ShipStatus result = converter.convertToEntityAttribute("Active");
        assertEquals(ShipStatus.ACTIVE, result);
    }

    @Test
    void testConvertToEntityAttribute_UnderMaintenance() {
        ShipStatus result = converter.convertToEntityAttribute("Under Maintenance");
        assertEquals(ShipStatus.UNDERMAINTENANCE, result);
    }

    @Test
    void testConvertToEntityAttribute_Null() {
        ShipStatus result = converter.convertToEntityAttribute(null);
        assertNull(result);
    }

    @Test
    void testConvertToEntityAttribute_Unknown() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute("Retired");
        });
        assertTrue(exception.getMessage().contains("Unknown Retired"));
    }

}