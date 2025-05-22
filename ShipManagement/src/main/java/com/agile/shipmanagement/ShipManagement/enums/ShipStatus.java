package com.agile.shipmanagement.ShipManagement.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ShipStatus {
    ACTIVE("Active"),
    UNDERMAINTENANCE("Under Maintenance");

    private final String value;

    ShipStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ShipStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ShipStatus value cannot be empty or null");
        }
        for (ShipStatus status : ShipStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown ShipStatus value: " + value);
    }

}
