package com.agile.shipmanagement.ShipManagement.utils;

import com.agile.shipmanagement.ShipManagement.model.ResponseModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class ResponseUtilTest {

    @Test
    void buildResponse_WithStringData() {
        // Arrange
        int statusCode = 200;
        String message = "Success";
        String data = "Test Data";

        // Act
        ResponseModel<String> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertNotNull(response);
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void buildResponse_WithObjectData() {
        // Arrange
        int statusCode = 201;
        String message = "Created";
        TestObject data = new TestObject("test");

        // Act
        ResponseModel<TestObject> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertNotNull(response);
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void buildResponse_WithNullData() {
        // Arrange
        int statusCode = 404;
        String message = "Not Found";
        Object data = null;

        // Act
        ResponseModel<Object> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertNotNull(response);
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
        ResponseModel<List<String>> response = ResponseUtil.buildResponse(statusCode, message, data);

        // Assert
        assertNotNull(response);
        assertEquals(statusCode, response.getStatusCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertEquals(3, response.getData().size());
    }

    // Helper class for testing with objects
    private static class TestObject {
        private String value;

        public TestObject(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}