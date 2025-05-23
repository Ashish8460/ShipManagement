package com.agile.shipmanagement.ShipManagement.model;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    // Getters and Setters
    private int statusCode;
    private String message;
    private T data;

}
