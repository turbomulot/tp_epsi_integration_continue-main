package com.epsi.tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    
    private String dbpassword = System.getenv("dbpassword");

    public boolean login(String username, String password) {
        LOGGER.log(Level.INFO, "Tentative de connexion de utilisateur : {0}", username);
        
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", dbpassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.log(Level.INFO, "Utilisateur connecté avec succès : {0}", username);
                    return true;
                } else {
                    LOGGER.log(Level.WARNING, "Identifiants invalides pour utilisateur : {0}", username);
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de utilisateur", e);
            return false;
        }
    }

    public void getUserDetails(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", dbpassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LOGGER.info("Utilisateur trouvé : " + rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de utilisateur", e);
        }
    }
    
    public void complexMethod(int a, int b, int c) {
        if (a <= 0) {
            LOGGER.info("A est négatif");
            return;
        }
        if (b <= 0) {
            if (c > 0) {
                LOGGER.info("B est négatif");
            } else {
                LOGGER.info("B et C sont négatifs");
            }
            return;
        }
        if (c <= 0) {
            LOGGER.info("C est négatif");
            return;
        }
        LOGGER.info("Tous positifs");
    }
}