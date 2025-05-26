package com.agile.shipmanagement.ShipManagement.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.agile.shipmanagement.ShipManagement.model.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private MethodParameter methodParameter;
    @Mock
    private HttpMessageNotReadableException httpMessageNotReadableException;
    @Mock
    private InvalidFormatException invalidFormatException;
    @Mock
    private JsonMappingException.Reference reference;


    private HttpMessageNotReadableException mockException;


    @BeforeEach
    void setUp() {
        openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
        mockException = mock(HttpMessageNotReadableException.class);

    }

    @Test
    void handleValidationException_WithFieldErrors() {
        // Arrange
        String expectedMessage = "Field validation failed";
        FieldError fieldError = new FieldError("object", "field", expectedMessage);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Act
        ResponseEntity<com.agile.shipmanagement.ShipManagement.model.ErrorResponse> response = exceptionHandler.handleValidationException(methodArgumentNotValidException);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    @Test
    void handleValidationException_WithNoFieldErrors() {
        // Arrange
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        // Act
        ResponseEntity<com.agile.shipmanagement.ShipManagement.model.ErrorResponse> response = exceptionHandler.handleValidationException(methodArgumentNotValidException);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        String message = "Invalid request message";

        if (cause == null) {
            return new ResponseEntity<>(new ErrorResponse(400, message), HttpStatus.BAD_REQUEST);
        }

        if (cause instanceof IllegalArgumentException) {
            message = cause.getMessage();
        } else if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                String fieldName = "";
                if (ife.getPath() != null && !ife.getPath().isEmpty()) {
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

    @Test
    void handleRuntimeException_WithConstraintViolation() {
        // Arrange
        RuntimeException runtimeException = new RuntimeException();
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(
                "Constraint violation", new SQLException("error in column \"test_column\""), "constraint"
        );
        runtimeException.initCause(constraintViolationException);

        // Act
        ResponseEntity<com.agile.shipmanagement.ShipManagement.model.ErrorResponse> response = exceptionHandler.handleRuntimeException(runtimeException);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Missing or invalid required field: 'test_column'", response.getBody().getMessage());
    }

    @Test
    void handleGenericException() {
        // Arrange
        Exception exception = new Exception("Some unexpected error");

        // Act
        ResponseEntity<com.agile.shipmanagement.ShipManagement.model.ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getMessage());
    }

    @Test
    void extractColumnNameFromSQLMessage_ValidMessage() {
        // Arrange
        String sqlMessage = "error in column \"test_column\" at row 1";

        // Act
        String result = exceptionHandler.extractColumnNameFromSQLMessage(sqlMessage);

        // Assert
        assertEquals("test_column", result);
    }

    @Test
    void extractColumnNameFromSQLMessage_InvalidMessage() {
        // Arrange
        String sqlMessage = "some invalid message";

        // Act
        String result = exceptionHandler.extractColumnNameFromSQLMessage(sqlMessage);

        // Assert
        assertEquals("unknown", result);
    }

    // Test enum for InvalidFormatException testing
    private enum TestEnum {
        VALUE1, VALUE2
    }



    @Test
    void handleHttpMessageNotReadableException_OtherException() {
        // Arrange
        Throwable mockCause = mock(Throwable.class);
        Throwable rootCause = mock(Throwable.class);
        when(mockException.getCause()).thenReturn(mockCause);
        when(mockCause.getCause()).thenReturn(rootCause);
        when(rootCause.getMessage()).thenReturn("Some other error");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadableException(mockException);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Some other error", errorResponse.getMessage());
    }

}
