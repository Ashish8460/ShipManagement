package com.agile.shipmanagement.ShipManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    // Getters and Setters
    private int statusCode;
    private String message;
    private T data;

}
