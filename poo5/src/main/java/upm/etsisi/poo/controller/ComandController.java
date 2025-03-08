package upm.etsisi.poo.controller;

import java.util.Scanner;
import upm.etsisi.poo.model.Admin;
import upm.etsisi.poo.model.Authentication;
import upm.etsisi.poo.model.ModelException;
import upm.etsisi.poo.view.PublicView;

public class ComandController {
    private static final Scanner sc = new Scanner(System.in);

    public static void start() {
        boolean initialized = DataController.initialitation();
        PublicView.welcome(initialized);
        if (initialized){
            boolean data = DataController.getData();
            if (data){
                boolean end = false;
                while (!end) {
                    end = menu();
                }
                DataController.close();
            } else PublicView.otherErrors("No se ha podido obtener los datos de la base de datos.");
        }
    }

    public static boolean menu() {
        PublicView.menu();
        String[] arguments = sc.nextLine().toLowerCase().split(" ", 2);
        String option = arguments[0];
        if (!option.equals("tournament-list")) {
            if (reviewArguments(arguments, 2)) arguments = arguments[1].split(";");
        }
        switch (option.toLowerCase()) {
            case "login":
                if (reviewArguments(arguments, 2)) {
                    Authentication.logIn(arguments[0], arguments[1]);
                    if (Authentication.getUserType()!=null){
                        if (Authentication.getUserType().equals("ADMIN")) {
                            menuAdmin();
                        } else menuPlayer();
                    }
                }
                break;
            case "tournament-list":
                TournamentsController.tournament_list(Authentication.getUserType());
                break;
            default:
                PublicView.otherErrors("No existe esa opción. Se procederá a finalizar la ejecución.");
                return true;
        }
        return false;
    }

    public static void menuAdmin() {
        while (Authentication.isLoggedIn()) {
            PublicView.menu();
            String[] arguments = sc.nextLine().toLowerCase().split(" ", 2);
            String option = arguments[0];
            if (!option.equals("logout") && !option.equals("tournament-list") && !option.equals("save-changes")) {
                if (reviewArguments(arguments, 2))
                    arguments = arguments[1].split(";");
                else arguments = new String[0];
            }
            switch (option.toLowerCase()) {
                case "player-create":
                    if (reviewArguments(arguments, 5)) {
                        try {
                            PlayersController.createPlayer(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], (Admin) Authentication.getCurrentUser());
                        } catch (ModelException modelException) {
                            PublicView.otherErrors(modelException.getMessage());
                        }
                    }
                    break;
                case "team-create":
                    if (reviewArguments(arguments, 1)) {
                        try {
                            TeamsController.createTeam(arguments[0], (Admin) Authentication.getCurrentUser());
                        } catch (ModelException modelException) {
                            PublicView.otherErrors(modelException.getMessage());
                        }
                    }
                    break;
                case "player-delete":
                    if (reviewArguments(arguments, 1)) {
                        PlayersController.deletePlayer(arguments[0]);
                    }
                    break;
                case "team-delete":
                    if (reviewArguments(arguments, 1)) {
                        TeamsController.deleteTeam(arguments[0]);
                    }
                    break;
                case "add-points":
                    if (reviewArguments(arguments, 3)) {
                        try {
                            double point = Double.parseDouble(arguments[2]);
                            PlayersController.addPoints(arguments[0], arguments[1], point);
                        } catch (Exception e) {
                            PublicView.otherErrors("\nLos puntos no están en un formato correcto");
                        }
                    }
                    break;
                case "team-add":
                    if (reviewArguments(arguments, 2)) {
                        TeamsController.teamAdd(arguments[0], arguments[1]);
                    }
                    break;
                case "team-remove":
                    if (reviewArguments(arguments, 2)) {
                        TeamsController.teamRemove(arguments[0], arguments[1]);
                    }
                    break;
                case "tournament-create":
                    if (reviewArguments(arguments, 6)) {
                        try {
                            TournamentsController.createTournament(arguments[0], arguments[1], arguments[2], arguments[3], arguments[4], arguments[5]);
                        } catch (ModelException modelException) {
                            PublicView.otherErrors(modelException.getMessage());
                        }
                    }
                    break;
                case "tournament-delete":
                    if (reviewArguments(arguments, 1)) {
                        TournamentsController.deleteTournament(arguments[0]);
                    }
                    break;
                case "tournament-matchmaking":
                    if (reviewArguments(arguments, 2)) {
                        String matchmaking = arguments[0];
                        switch (matchmaking) {
                            case "-m":
                                if (reviewArguments(arguments, 4)) {
                                    int i = 0;
                                    while (i < 3) {
                                        arguments[i] = arguments[i + 1];
                                        i++;
                                    }
                                    TournamentsController.tournamentMatchmakingM(arguments[0], arguments[1], arguments[2]);
                                }
                                break;
                            case "-a":
                                if (reviewArguments(arguments, 2)) {
                                    arguments[0] = arguments[1];
                                    TournamentsController.tournamentMatchmakingA(arguments[0]);
                                }
                                break;
                            default:
                                PublicView.otherErrors("\nNo existe esa opcion de formato");
                                break;
                        }
                    }
                    break;
                case "tournament-list":
                    TournamentsController.tournament_list(Authentication.getUserType());
                    break;
                case "save-changes":
                    DataController.saveData();
                    break;
                case "logout":
                    Authentication.logOut();
                    break;
                default:
                    PublicView.otherErrors("No existe esa opción.");
                    break;
            }
        }
    }

    public static void menuPlayer() {
        while (Authentication.isLoggedIn()) {
            PublicView.menu();
            String[] arguments = sc.nextLine().toLowerCase().split(" ", 2);
            String option = arguments[0];
            if (!option.equals("logout") && !option.equals("tournament-list") && !option.equals("save-changes")) {
                if (reviewArguments(arguments, 2))
                    arguments = arguments[1].split(";");
                else arguments = new String[0];
            }
            switch (option.toLowerCase()) {
                case "tournament-add":
                    if (reviewArguments(arguments, 1)) {
                        if (arguments.length > 1){
                            TournamentsController.tournamentAdd(arguments[0], arguments[1]);
                        } else {
                            TournamentsController.tournamentAdd(arguments[0], null);
                        }
                    }
                    break;
                case "tournament-remove":
                    if (reviewArguments(arguments, 1)) {
                        if (arguments.length > 1){
                            TournamentsController.tournamentRemove(arguments[0], arguments[1]);
                        } else {
                            TournamentsController.tournamentRemove(arguments[0], null);
                        }
                    }
                    break;
                case "statistics-show":
                    if (reviewArguments(arguments, 1)) {
                        try {
                            PlayersController.statisticsShow(arguments[0]);
                        } catch (IllegalArgumentException e) {
                            PublicView.otherErrors("\nNo ha sido posible mostrar las estadisticas.");
                        }
                    }
                    break;
                case "tournament-list":
                    TournamentsController.tournament_list(Authentication.getUserType());
                    break;
                case "save-changes":
                    DataController.saveData();
                    break;
                case "logout":
                    Authentication.logOut();
                    break;
                default:
                    PublicView.otherErrors("No existe esa opción.");
                    break;
            }
        }
    }

    private static boolean reviewArguments(String[] arguments, int number) {
        if (arguments.length >= number) {
            return true;
        } else {
            PublicView.otherErrors("\nMuy pocos argumentos");
            return false;
        }
    }
}
