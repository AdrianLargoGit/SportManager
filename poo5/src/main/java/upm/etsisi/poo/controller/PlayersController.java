package upm.etsisi.poo.controller;

import upm.etsisi.poo.model.Admin;
import upm.etsisi.poo.model.Authentication;
import upm.etsisi.poo.model.ModelException;
import upm.etsisi.poo.model.Player;
import upm.etsisi.poo.view.AdminView;
import upm.etsisi.poo.view.PlayerView;
import upm.etsisi.poo.view.PublicView;

import java.util.ArrayList;

public class PlayersController {
    private static PlayersController uniqueInstance;
    private static ArrayList<Player> players;

    private PlayersController() {
        this.players = new ArrayList<>();
    }

    public static PlayersController getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new PlayersController();
        }
        return uniqueInstance;
    }

    public static void addPlayer(Player player){
        players.add(player);
    }

    public static Player getPlayer(String username) {
        Player result = null;
        int i = 0;
        if (!players.isEmpty()) {
            while (i < players.size() && players.get(i) != null && result == null) {
                if (username.equals(players.get(i).getUsername())) result = players.get(i);
                i++;
            }
        }
        return result;
    }

    public static void createPlayer(String username, String password, String name, String surname, String dni, Admin creator) throws ModelException {
        if (AdminsController.getAdmin(username) == null && getPlayer(username) == null && TeamsController.getTeam(username) == null) {
            players.add(new Player(username, password, name, surname, dni, creator));
            AdminView.player_create(true);
        } else AdminView.player_create(false);
    }

    public static void deletePlayer(String username) {
        if (getPlayer(username) != null) {
            if (TeamsController.isInTeam(username) != null) {
                if (TournamentsController.isParticipant(TeamsController.isInTeam(username)) == null) {
                    TeamsController.isInTeam(username).removePlayer(username);
                    players.remove(getPlayer(username));
                    AdminView.player_delete(false, false, true);
                } else AdminView.player_delete(true, false, false);;
            } else if (TournamentsController.isParticipant(getPlayer(username)) == null) {
                players.remove(getPlayer(username));
                AdminView.player_delete(false, false, true);;
            } else AdminView.player_delete(false, true, false);;
        } else AdminView.player_delete(false, false, false);;
    }

    public static void addPoints(String username, String stat, double points) {
        if (getPlayer(username) != null) {
            if (TeamsController.isInTeam(username)!=null){
                TeamsController.isInTeam(username).updateStats();
            }
            AdminView.add_points(getPlayer(username).updateStat(stat, points));
        } else AdminView.add_points(false);
    }

    public static void statisticsShow(String option) {
        if (option.equals("-csv")) {
            PlayerView.statistics_showCSV(((Player) Authentication.getCurrentUser()).getStats());
        } else if (option.equals("-json")) {
            PlayerView.statistics_showJson(((Player) Authentication.getCurrentUser()).getStats());
        } else PublicView.otherErrors("\nAsegurate de que la opcion sea valida");
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }
}
