package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.model.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");

        return new ResponseEntity<>(new ErrorResponse(400, message), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        String message = cause.getCause().getMessage();

        // If the root cause is your IllegalArgumentException (from enum @JsonCreator)
        if (cause instanceof IllegalArgumentException) {
            message = cause.getMessage();  // This will be "ShipStatus value cannot be empty or null"
        } else if (cause instanceof InvalidFormatException) {
            // Optional: handle enum invalid value cases
            InvalidFormatException ife = (InvalidFormatException) cause;
            if (ife.getTargetType().isEnum()) {
                String fieldName = "";
                if (!ife.getPath().isEmpty()) {
                    fieldName = ife.getPath().get(0).getFieldName();
                }
                String invalidValue = ife.getValue() == null ? "null" : ife.getValue().toString();

                message = String.format(
                        "Invalid value '%s' for field '%s'. Allowed values are %s.",
                        invalidValue,
                        fieldName,
                        java.util.Arrays.toString(ife.getTargetType().getEnumConstants())
                );
            }
        }

        return new ResponseEntity<>(new ErrorResponse(400, message), HttpStatus.BAD_REQUEST);
    }

    public String extractColumnNameFromSQLMessage(String msg) {
        try {
            int start = msg.indexOf("column \"") + 8;
            int end = msg.indexOf("\"", start);
            return msg.substring(start, end);
        } catch (Exception e) {
            return "unknown";
        }
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();  // Default to top-level message

        Throwable cause = ex.getCause();

        // Try to get cause message if available safely
        if (cause != null && cause.getMessage() != null) {
            message = cause.getMessage();
        }

        // Optional: If you want to specifically handle InvalidFormatException somewhere deeper
        Throwable rootCause = cause;
        while (rootCause != null) {
            if (rootCause instanceof InvalidFormatException) {
                InvalidFormatException ife = (InvalidFormatException) rootCause;
                if (ife.getTargetType().isEnum()) {
                    String fieldName = "";
                    if (!ife.getPath().isEmpty()) {
                        fieldName = ife.getPath().get(0).getFieldName();
                    }
                    String invalidValue = ife.getValue() == null ? "null" : ife.getValue().toString();

                    message = String.format(
                            "Invalid value '%s' for field '%s'. Allowed values are %s.",
                            invalidValue,
                            fieldName,
                            java.util.Arrays.toString(ife.getTargetType().getEnumConstants())
                    );
                    break;
                }
            } else if (Objects.equals(ex.getCause().getClass(), org.hibernate.exception.ConstraintViolationException.class)) {
                org.hibernate.exception.ConstraintViolationException cve =
                        (org.hibernate.exception.ConstraintViolationException) ex.getCause();
                String sqlMessage = cve.getSQLException().getMessage();
                String column = extractColumnNameFromSQLMessage(sqlMessage);

                message = "Missing or invalid required field: '" + column + "'";

                return ResponseEntity.badRequest().body(new ErrorResponse(400, message));
            }
            rootCause = rootCause.getCause();
        }

        return new ResponseEntity<>(new ErrorResponse(400, message), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(500, "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
