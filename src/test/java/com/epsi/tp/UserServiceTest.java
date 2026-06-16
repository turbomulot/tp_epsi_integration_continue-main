package com.epsi.tp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

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

    // ==========================================
    // TESTS DE LA MÉTHODE : login()
    // ==========================================

    @Test
    public void testLogin_Success() throws SQLException {
        // Préparation des Mocks (Fausse base de données)
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true); // On simule qu'un utilisateur est trouvé

        // Interception du DriverManager
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), any()))
                    .thenReturn(mockConn);

            boolean result = userService.login("admin", "password");
            assertTrue(result, "Le login devrait réussir");
        }
    }

    @Test
    public void testLogin_Failure() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false); // On simule qu'aucun utilisateur n'est trouvé

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), any()))
                    .thenReturn(mockConn);

            boolean result = userService.login("admin", "wrong_password");
            assertFalse(result, "Le login devrait échouer");
        }
    }

    @Test
    public void testLogin_SqlException() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            // On force une exception lors de la connexion
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), any()))
                    .thenThrow(new SQLException("Erreur de connexion simulée"));

            boolean result = userService.login("admin", "password");
            assertFalse(result, "Le login devrait échouer à cause de l'exception");
        }
    }

    // ==========================================
    // TESTS DE LA MÉTHODE : getUserDetails()
    // ==========================================

    @Test
    public void testGetUserDetails_Success() throws SQLException {
        Connection mockConn = mock(Connection.class);
        PreparedStatement mockStmt = mock(PreparedStatement.class);
        ResultSet mockRs = mock(ResultSet.class);

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockRs);
        // On simule une boucle while(rs.next()) qui fait 1 tour
        when(mockRs.next()).thenReturn(true).thenReturn(false);
        when(mockRs.getString("username")).thenReturn("john_doe");

        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), any()))
                    .thenReturn(mockConn);

            assertDoesNotThrow(() -> userService.getUserDetails("john_doe"));
        }
    }

    @Test
    public void testGetUserDetails_SqlException() throws SQLException {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), any()))
                    .thenThrow(new SQLException("Erreur simulée"));

            // On vérifie que l'erreur est bien attrapée (catch) et ne fait pas planter le programme
            assertDoesNotThrow(() -> userService.getUserDetails("john_doe"));
        }
    }

    // ==========================================
    // TESTS DE LA MÉTHODE : complexMethod()
    // ==========================================

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