package upm.etsisi.poo.controller;

import upm.etsisi.poo.model.Admin;
import upm.etsisi.poo.model.ModelException;
import upm.etsisi.poo.model.Player;
import upm.etsisi.poo.model.Team;
import upm.etsisi.poo.view.AdminView;

import java.util.ArrayList;

public class TeamsController {
    private static TeamsController uniqueInstance;
    private static ArrayList<Team> teams;

    private TeamsController() {
        this.teams = new ArrayList<>();
    }

    public static TeamsController getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new TeamsController();
        }
        return uniqueInstance;
    }

    public static void addTeam(Team team){
        teams.add(team);
    }

    public static Team getTeam(String name) {
        Team result = null;
        int i = 0;
        while (i < teams.size() && result == null) {
            if (teams.get(i).getName().equalsIgnoreCase(name)) {
                result = teams.get(i);
            }
            i++;
        }
        return result;
    }

    public static void createTeam(String name, Admin creator) throws ModelException {
        if (getTeam(name) == null && PlayersController.getPlayer(name) == null) {
            teams.add(new Team(name, creator));
            AdminView.team_create(true);
        } else AdminView.team_create(false);
    }

    public static void deleteTeam(String name) {
        if (getTeam(name) != null) {
            if (TournamentsController.isParticipant(getTeam(name)) == null) {
                for (Player player : getTeam(name).getPlayers()){
                    player.setTeam(null);
                }
                AdminView.team_delete(false, teams.remove(getTeam(name)));
            } else AdminView.team_delete(true, false);
        } else AdminView.team_delete(false, false);
    }

    public static Team isInTeam(String username) {
        Team team = null;
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getPlayer(username) != null) {
                team = teams.get(i);
            }
        }
        return team;
    }

    public static void teamAdd(String username, String team) {
        if (PlayersController.getPlayer(username) != null) {
            if (getTeam(team) != null) {
                if (isInTeam(username) == null) {
                    AdminView.team_add(false, false, getTeam(team).addPlayer(PlayersController.getPlayer(username)));
                } else AdminView.team_add(true, false, false);
            } else AdminView.team_add(false, true, false);
        } else AdminView.team_add(false, false, false);
    }


    public static void teamRemove(String username, String team) {
        if (PlayersController.getPlayer(username) != null) {
            if (getTeam(team) != null) {
                if (isInTeam(username).equals(getTeam(team))) {
                    AdminView.team_remove(false, false, getTeam(team).removePlayer(username));
                } else AdminView.team_remove(false, true, false);
            } else AdminView.team_remove(true, false, false);
        } else AdminView.team_remove(false, false, false);
    }

    public static ArrayList<Team> getTeams() {
        return teams;
    }
}
