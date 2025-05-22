package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.model.ResponseModel;

public class ResponseUtil {

    public static <T> ResponseModel<T> buildResponse(int statusCode, String message, T data) {
        ResponseModel<T> response = new ResponseModel<>();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
