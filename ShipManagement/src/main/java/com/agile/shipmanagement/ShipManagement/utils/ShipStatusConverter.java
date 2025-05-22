package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.enums.ShipStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShipStatusConverter implements AttributeConverter<ShipStatus, String> {

    @Override
    public String convertToDatabaseColumn(ShipStatus status) {
        if (status == null) return null;
        return switch (status) {
            case ACTIVE -> "Active";
            case UNDERMAINTENANCE -> "Under Maintenance";
            default -> throw new IllegalArgumentException("Unknown " + status + "Values Should be Active or Under Maintenance");
        };
    }

    @Override
    public ShipStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return switch (dbData) {
            case "Active" -> ShipStatus.ACTIVE;
            case "Under Maintenance" -> ShipStatus.UNDERMAINTENANCE;
            default -> throw new IllegalArgumentException("Unknown " + dbData + "Values Should be Active or Under Maintenance");
        };
    }
}
