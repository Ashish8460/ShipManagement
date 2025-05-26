package com.agile.shipmanagement.ShipManagement.enums;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ShipTypeJsonTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getValue_ShouldReturnCorrectValue() {
        assertEquals("Cargo", ShipType.CARGO.getValue());
        assertEquals("Tanker", ShipType.TANKER.getValue());
    }

    @Test
    void fromValue_ShouldReturnCorrectEnum() {
        assertEquals(ShipType.CARGO, ShipType.fromValue("Cargo"));
        assertEquals(ShipType.TANKER, ShipType.fromValue("Tanker"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"cargo", "CARGO", "CaRgO"})
    void fromValue_ShouldBeCaseInsensitive(String input) {
        assertEquals(ShipType.CARGO, ShipType.fromValue(input));
    }

    @Test
    void fromValue_ShouldThrowException_WhenInvalidValue() {
        String invalidValue = "InvalidType";
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ShipType.fromValue(invalidValue)
        );
        assertEquals("Unknown ShipType value: " + invalidValue, exception.getMessage());
    }

    @Test
    void jsonSerialization_ShouldGenerateCorrectJson() throws Exception {
        String json = objectMapper.writeValueAsString(ShipType.CARGO);
        assertEquals("\"Cargo\"", json);
    }

    @Test
    void jsonDeserialization_ShouldCreateCorrectEnum() throws Exception {
        ShipType shipType = objectMapper.readValue("\"Cargo\"", ShipType.class);
        assertEquals(ShipType.CARGO, shipType);
    }

    @Test
    void jsonDeserialization_ShouldBeCaseInsensitive() throws Exception {
        ShipType shipType1 = objectMapper.readValue("\"cargo\"", ShipType.class);
        ShipType shipType2 = objectMapper.readValue("\"CARGO\"", ShipType.class);

        assertEquals(ShipType.CARGO, shipType1);
        assertEquals(ShipType.CARGO, shipType2);
    }

    @Test
    void jsonDeserialization_ShouldThrowException_WhenInvalidValue() {
        assertThrows(Exception.class, () ->
                objectMapper.readValue("\"InvalidType\"", ShipType.class)
        );
    }
}
