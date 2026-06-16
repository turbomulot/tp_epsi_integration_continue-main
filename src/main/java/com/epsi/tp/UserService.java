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

    public void login(String username, String password) {
        LOGGER.info("Tentative de connexion de l'utilisateur : " + username);

        // Simulation basique sans mot de passe en dur
        if ("admin".equals(username)) {
            LOGGER.info("Administrateur connecté avec succès.");
        } else {
            LOGGER.warning("Identifiants invalides.");
        }
    }

public void getUserDetails(String username) {
        String dbPassword = System.getenv("DB_PASSWORD");
        if (dbPassword == null) {
            dbPassword = ""; 
        }

        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LOGGER.info("Utilisateur trouvé : " + rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'accès à la base de données", e);
        }
    }
}
