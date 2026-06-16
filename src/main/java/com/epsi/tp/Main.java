package com.epsi.tp;

public class Main {
    public static void main(String[] args) {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    LOGGER.info("Démarrage de l'application...");
        
        UserService userService = new UserService();
        userService.login("admin", "password123");
        userService.getUserDetails("john_doe");
    }
}
