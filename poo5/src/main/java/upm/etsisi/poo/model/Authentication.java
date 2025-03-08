package upm.etsisi.poo.model;

import upm.etsisi.poo.controller.AdminsController;
import upm.etsisi.poo.controller.PlayersController;
import upm.etsisi.poo.view.PublicView;

public class Authentication {
    private static final String ATTR_CURRENTUSER_NAME = "currentUser";
    private static final String ATTR_USERTYPE_NAME = "userType";
    private static Authentication uniqueInstance;
    private static User currentUser;
    private static UserType userType;

    public enum UserType {
        ADMIN, PLAYER
    }

    private Authentication() {
        currentUser = null;
        userType = null;
    }

    public static Authentication getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Authentication();
        }
        return uniqueInstance;
    }

    public static void logIn(String username, String password) {
        if (!isLoggedIn()) {
            if (AdminsController.getAdmin(username) != null) {
                if (AdminsController.getAdmin(username).getPassword().equals(password)) {
                    currentUser = AdminsController.getAdmin(username);
                    userType = UserType.ADMIN;
                }
            } else {
                if (PlayersController.getPlayer(username) != null) {
                    if (PlayersController.getPlayer(username).getPassword().equals(password)) {
                        currentUser = PlayersController.getPlayer(username);
                        userType = UserType.PLAYER;
                    }
                }
            }
        }
        PublicView.login(userType, username);
    }

    public static void logOut() {
        userType = null;
        currentUser = null;
        PublicView.logout(userType);
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getUserType() {
        if (userType != null) return userType.name();
        else return null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
}
