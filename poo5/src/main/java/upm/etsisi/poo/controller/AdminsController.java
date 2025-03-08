package upm.etsisi.poo.controller;

import upm.etsisi.poo.model.Admin;

import java.util.ArrayList;

public class AdminsController {
    private static AdminsController uniqueInstance;
    private static ArrayList<Admin> admins;

    private AdminsController() {
        this.admins = new ArrayList<>();
    }

    public static Admin getAdmin(String username) {
        Admin result = null;
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUsername().equalsIgnoreCase(username)) {
                result = admins.get(i);
            }
        }
        return result;
    }

    public static AdminsController getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new AdminsController();
        }
        return uniqueInstance;
    }

    public static void addAdmin(Admin admin) {
        if (getAdmin(admin.getUsername()) == null) {
            admins.add(admin);
        }
    }

    public static ArrayList<Admin> getAdmins() {
        return admins;
    }
}
