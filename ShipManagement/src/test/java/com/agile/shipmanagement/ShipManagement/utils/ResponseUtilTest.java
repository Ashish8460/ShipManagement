package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.model.ResponseModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilTest {

    @Test
    void buildResponse_WithStringData() {
        // Arrange
        int statusCode = 200;
        String message = "Success";
        String data = "Test Data";

        // Act
        com.agile.shipmanagement.ShipManagement.model.ResponseModel<String> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void buildResponse_WithObjectData() {
        // Arrange
        int statusCode = 201;
        String message = "Created";
        TestObject data = new TestObject("test", 123);

        // Act
        com.agile.shipmanagement.ShipManagement.model.ResponseModel<TestObject> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void buildResponse_WithNullData() {
        // Arrange
        int statusCode = 204;
        String message = "No Content";
        String data = null;

        // Act
        com.agile.shipmanagement.ShipManagement.model.ResponseModel<String> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void buildResponse_WithListData() {
        // Arrange
        int statusCode = 200;
        String message = "Success";
        List<String> data = Arrays.asList("item1", "item2", "item3");

        // Act
        com.agile.shipmanagement.ShipManagement.model.ResponseModel<List<String>> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertEquals(3, response.getData().size());
    }

    @Test
    void buildResponse_WithEmptyMessage() {
        // Arrange
        int statusCode = 200;
        String message = "";
        String data = "Test Data";

        // Act
        com.agile.shipmanagement.ShipManagement.model.ResponseModel<String> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    // Test helper class
    private static class TestObject {
        private String name;
        private int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return value == that.value && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }
}
