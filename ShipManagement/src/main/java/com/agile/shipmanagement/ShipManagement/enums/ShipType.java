package com.agile.shipmanagement.ShipManagement.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ShipType {
    CARGO("Cargo"),
    TANKER("Tanker"),
    CRUISE("Cruise"),
    FERRY("Ferry"),
    WARSHIP("Warship");

    private final String value;

    ShipType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ShipType fromValue(String value) {
        for (ShipType type : ShipType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ShipType value: " + value );
    }
}


