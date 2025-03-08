package upm.etsisi.poo.view;

import upm.etsisi.poo.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublicView {
    public static void welcome(boolean success){
        if (success){
            System.out.println("\n----------BIENVENIDO AL SISTEMA DE GESTIÓN DEPORTIVA ----------\n");
        } else System.out.println("No se ha podido iniciar correctamente");
    }
    public static void menu(){
        StringBuilder menu = new StringBuilder();
        if (Authentication.getUserType()==null){
            menu.append("Comandos:\n > login [username;password]\n > tournament-list\n----------\n\t> ");
        } else if (Authentication.getUserType().equals("ADMIN")){
            menu.append("Comandos:\n > player-create [username;password;name;surname;dni]\n > team-create [name]\n > player-delete [username]\n > team-delete [name]\n > add-points [player username;category;points]\n > team-add [player username;team]\n > team-remove [player username;team]\n > tournament-create [name;startDate;endDate;league;sport;categoryRank]\n > tournament-delete [tournament name]\n > tournament-matchmaking [-m/-a;tournament name(;team1;team2)]\n > tournament-list\n > save-changes\n > logout\n----------\n\t> ");
        } else if (Authentication.getUserType().equals("PLAYER")){
            menu.append("Comandos:\n > tournament-add [tournament name(;team)]\n > tournament-remove [tournament name(;team)]\n > statistics-show [-csv/-json]\n > tournament-list\n > save-changes\n > logout\n----------\n\t> ");
        } else menu.append("No existe ese tipo de usuario");
        System.out.print(menu);}

    public static void login(Authentication.UserType type, String username){
        StringBuilder message = new StringBuilder();
        if (type==null){
            message.append("\nError al hacer login. Campos introducidos erróneos.");
        } else if (type.toString().equals("ADMIN")){
            message.append("\nROL: admin\nBIENVENIDO, ").append(username);
        } else if (type.toString().equals("PLAYER")) {
            message.append("\nROL: player\nBIENVENIDO, ").append(username);
        }
        System.out.println(message);
    }

    public static void logout(Authentication.UserType type){
        if (type==null){
            System.out.println("\nCERRANDO SESIÓN...");
        } else System.out.println("\nError al cerrar sesión.");
    }

    public static void tournamentList(HashMap<Tournament, ArrayList<Participant>> tournaments){
        StringBuilder list = new StringBuilder();
        if (tournaments.isEmpty()){
            list.append("\nNo existen torneos a listar");
        } else {
            for (Map.Entry<Tournament, ArrayList<Participant>> tournament : tournaments.entrySet()){
                list.append("\nNOMBRE: ").append(tournament.getKey().getName()).append("\nFECHA: ").append(tournament.getKey().getStartDate()).append(" - ").append(tournament.getKey().getEndDate());
                for (int i = 0; i<tournament.getValue().size(); i++){
                    if (tournament.getValue().get(i) instanceof Player){
                        list.append("\n\t\tJugador: ").append(((Player) tournament.getValue().get(i)).getUsername()).append("\n\t\t").append(tournament.getValue().get(i).getStat(tournament.getKey().getCategoryRank().name()));
                    } else if (tournament.getValue().get(i) instanceof Team) {
                        list.append("\n\t\tEquipo: ").append(tournament.getValue().get(i).getName()).append("\n\t\t").append(tournament.getValue().get(i).getStat(tournament.getKey().getCategoryRank().name()));
                    }
                }
            }
        }
        System.out.println(list);
    }

    public static void otherErrors(String message){
        System.out.println(message);
    }
    public static void saveData(boolean saved){
        if (saved){
            System.out.println("\nCAMBIOS GUARDADOS CORRECTAMENTE");
        } else System.out.println("\nNo se han podido guardar los cambios");
    }
}
