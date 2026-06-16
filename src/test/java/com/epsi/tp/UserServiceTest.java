package com.epsi.tp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testLogin_Success() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            // Utilisation de any() au lieu de anyString() pour accepter le mot de passe null de Jenkins
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any()))
                    .thenReturn(mockConn);

            boolean result = userService.login("admin", "password");
            assertTrue(result);
        }
    }

    @Test
    public void testLogin_Failure() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any()))
                    .thenReturn(mockConn);

            boolean result = userService.login("admin", "wrong_password");
            assertFalse(result);
        }
    }

    @Test
    public void testLogin_SqlException() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any()))
                    .thenThrow(SQLException.class);

            boolean result = userService.login("admin", "password");
            assertFalse(result);
        }
    }

    @Test
    public void testGetUserDetails_Success() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true).thenReturn(false);
        when(mockRs.getString("username")).thenReturn("john_doe");

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any()))
                    .thenReturn(mockConn);

            assertDoesNotThrow(() -> userService.getUserDetails("john_doe"));
        }
    }

    @Test
    public void testGetUserDetails_SqlException() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(any(), any(), any()))
                    .thenThrow(SQLException.class);

            assertDoesNotThrow(() -> userService.getUserDetails("john_doe"));
        }
    }

    @Test
    public void testComplexMethod_ALessThanZero() {
        assertDoesNotThrow(() -> userService.complexMethod(0, 1, 1));
    }

    @Test
    public void testComplexMethod_BLessThanZero_CGreaterThanZero() {
        assertDoesNotThrow(() -> userService.complexMethod(1, 0, 1));
    }

    @Test
    public void testComplexMethod_BLessThanZero_CLessThanZero() {
        assertDoesNotThrow(() -> userService.complexMethod(1, 0, 0));
    }

    @Test
    public void testComplexMethod_BGreaterThanZero_CLessThanZero() {
        assertDoesNotThrow(() -> userService.complexMethod(1, 1, 0));
    }

    @Test
    public void testComplexMethod_AllPositive() {
        assertDoesNotThrow(() -> userService.complexMethod(1, 1, 1));
    }
}