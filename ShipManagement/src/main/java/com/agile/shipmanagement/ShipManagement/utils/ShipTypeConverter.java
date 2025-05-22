package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.enums.ShipType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ShipTypeConverter implements AttributeConverter<ShipType, String> {

    @Override
    public String convertToDatabaseColumn(ShipType attribute) {
        if (attribute == null) return null;
        return attribute.getValue(); // store the custom string value, e.g., "Cargo"
    }

    @Override
    public ShipType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        for (ShipType type : ShipType.values()) {
            if (type.getValue().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ShipType value: " + dbData);
    }
}
