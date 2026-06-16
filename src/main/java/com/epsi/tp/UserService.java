package com.epsi.tp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private String DB_PASSWORD = System.getenv("DB_PASSWORD");
    
    public void login(String username, String password) {
        
        LOGGER.info("Tentative de connexion de l'utilisateur : " + username);

    public boolean login(String username, String password) {
        LOGGER.info("Tentative de connexion de l'utilisateur : " + username);
        
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", DB_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.info("Utilisateur connecté avec succès : " + username);
                    return true; 
                } else {
                    LOGGER.warn("Identifiants invalides pour l'utilisateur : " + username);
                    return false; 
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de la connexion", e);
            return false;
        }
}
        
        try {
            // Logique factice pour déclencher une exception
            int result = 10 / 0;
        } catch (Exception e) {
            // Mauvaise pratique : bloc catch vide (l'erreur est ignorée silencieusement)
            LOGGER.error("Erreur lors de la tentative de connexion", e);
        }
    }

    public void getUserDetails(String username) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", DB_PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LOGGER.info("Utilisateur trouvé : " + rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'utilisateur", e);
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
