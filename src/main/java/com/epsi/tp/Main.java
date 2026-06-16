package com.epsi.tp;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Démarrage de l'application...");
        
        UserService userService = new UserService();
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        
        if (adminPassword != null && !adminPassword.isEmpty()) {
            userService.login("admin", adminPassword);
        } else {
            LOGGER.warning("Mot de passe non fourni dans l'environnement. Connexion ignorée.");
        }
        
        userService.getUserDetails("john_doe");
    }
}