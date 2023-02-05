package com.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

/**
 * Unit tests
 */
public class PostCoordinates {


    private static TestClient testClient = null;
    private static TestSettings testSettings = null;

    PostCoordinates(){
        testSettings = new TestSettings();
        testSettings.readSettingsXML("testconfigw1.xml");
        testClient = new TestClient(testSettings.getServerAddress());
        System.out.println("initialized week 1 tests");
    }

    @Test
    @BeforeAll
    @DisplayName("Setting up the test environment")
    public static void initialize() {

    }

    @Test 
    @Order(1)
    @DisplayName("Testing server connection")
    void testHTTPServerConnection() throws IOException {
        System.out.println("Testing server connection");
        int result = testClient.testConnection();
        assertTrue(result == 200 || result == 204);
    }

    @Test 
    @Order(2)
    @DisplayName("Testing sending coordinates to server")
    void testSendCoordinates() throws IOException {
        System.out.println("Testing sending coordinates to server");
        String coordinates = "Laivala";
        int result = testClient.testMessage(coordinates);
        assertTrue(result == 200 || result == 204);
        String response = testClient.getMessages();

        //search for a specific response in a string

        Boolean found = false;

        found = response.contains(coordinates);

        assertTrue(found);
    }

    @Test 
    @Order(3)
    @DisplayName("Testing characters")
    void testCharacters() throws IOException {
        System.out.println("Testing characters");
        String coordinates = "åäö";
        int result = testClient.testMessage(coordinates);
        assertTrue(result == 200 || result == 204);
        String response = testClient.getMessages();

        //search for a specific response in a string

        Boolean found = false;

        found = response.contains(coordinates);

        assertTrue(found);
    }

}
