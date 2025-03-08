package upm.etsisi.poo.controller;

import java.util.ArrayList;
import java.util.HashMap;

import upm.etsisi.poo.model.*;
import upm.etsisi.poo.view.AdminView;
import upm.etsisi.poo.view.PlayerView;
import upm.etsisi.poo.view.PublicView;


public class TournamentsController {
    //Borra la linea 112 el codigo:  && getTournament(name).getStartDate().lowerThan(new Date())
    //para que te deje añadir matchmaking automaticos sin que haya empezado el torneo
    private static TournamentsController uniqueInstance;
    private static ArrayList<Tournament> tournaments;

    private TournamentsController() {
        tournaments = new ArrayList<>();
    }

    public static TournamentsController getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new TournamentsController();
        }
        return uniqueInstance;
    }

    public static Tournament getTournament(String name) {
        for (Tournament tournament : tournaments) {
            if (tournament.getName().equals(name)) {
                return tournament;
            }
        }
        return null;
    }


    public static void deleteTournament(String name) {
        if (getTournament(name) != null) {
            for (Participant participant : getTournament(name).getParticipants()){
                participant.deleteTournament(getTournament(name));
                for (Matchmaking matchmaking : getTournament(name).getMatchmaking().getMatchmaking()){
                    participant.getMatchmakings().remove(matchmaking);
                }
            }
            tournaments.remove(getTournament(name));
            AdminView.tournament_delete(true);
        } else AdminView.tournament_delete(false);
    }

    public static void deletePastTournaments() {
        for (int i = tournaments.size()-1; i >= 0; i--) {
            if (tournaments.get(i).getEndDate().lowerThan(new Date())) {
                tournaments.remove(i);
                i--;
            }
        }
    }

    public static Tournament isParticipant(Participant participant){
        Tournament result = null;
        for (int i = 0; i< tournaments.size();i++){
            if (tournaments.get(i).getParticipant(participant)!=null){
                result = tournaments.get(i);
            }
        }
        return result;
    }


    public static void createTournament(String name, String startDate, String endDate, String league, String sport, String categoryRank) throws ModelException {
        if (getTournament(name)==null){
            if (Date.isCorrect(startDate)&& Date.isCorrect(endDate)){
                Date date1 = new Date(startDate);
                Date date2 = new Date(endDate);
                if (date1.greaterThan(new Date()) && date2.greaterThan(date1) && Categories.getCategory(categoryRank)!=null){
                    tournaments.add(new Tournament(name, date1, date2, league, sport, Categories.getCategory(categoryRank).name()));
                    AdminView.tournament_create(false, false, true);
                } else AdminView.tournament_create(false, true, false);
            } else AdminView.tournament_create(true, false, false);
        } else AdminView.tournament_create(false, false, false);
    }

    public static void tournamentMatchmakingM(String name, String participant1, String participant2){
        if (getTournament(name)!=null && getTournament(name).getStartDate().lowerThan(new Date())){
            if (PlayersController.getPlayer(participant1)!=null){
                if (PlayersController.getPlayer(participant2)!=null){
                    if (getTournament(name).getParticipant(PlayersController.getPlayer(participant1)) != null && getTournament(name).getParticipant(PlayersController.getPlayer(participant2)) != null){
                        AdminView.tournament_matchmakingM(false, false, getTournament(name).getMatchmaking().createMatchmake(PlayersController.getPlayer(participant1), PlayersController.getPlayer(participant2), getTournament(name)));
                    } else AdminView.tournament_matchmakingM(true, false, false);
                } else if (TeamsController.getTeam(participant2)!=null){
                    if (getTournament(name).getParticipant(PlayersController.getPlayer(participant1)) != null && getTournament(name).getParticipant(TeamsController.getTeam(participant2)) != null){
                        AdminView.tournament_matchmakingM(false, false, getTournament(name).getMatchmaking().createMatchmake(PlayersController.getPlayer(participant1), TeamsController.getTeam(participant2), getTournament(name)));
                    } else AdminView.tournament_matchmakingM(true, false, false);
                } else AdminView.tournament_matchmakingM(false, true, false);
            } else if (TeamsController.getTeam(participant1)!=null){
                if (PlayersController.getPlayer(participant2)!=null){
                    if (getTournament(name).getParticipant(TeamsController.getTeam(participant1)) != null && getTournament(name).getParticipant(PlayersController.getPlayer(participant2)) != null){
                        AdminView.tournament_matchmakingM(false, false, getTournament(name).getMatchmaking().createMatchmake(TeamsController.getTeam(participant1), PlayersController.getPlayer(participant2), getTournament(name)));
                    } else AdminView.tournament_matchmakingM(true, false, false);
                } else if (TeamsController.getTeam(participant2)!=null){
                    if (getTournament(name).getParticipant(TeamsController.getTeam(participant1)) != null && getTournament(name).getParticipant(TeamsController.getTeam(participant2)) != null){
                        AdminView.tournament_matchmakingM(false, false, getTournament(name).getMatchmaking().createMatchmake(TeamsController.getTeam(participant1), TeamsController.getTeam(participant2), getTournament(name)));
                    } else AdminView.tournament_matchmakingM(true, false, false);
                } else AdminView.tournament_matchmakingM(false, true, false);
            } else AdminView.tournament_matchmakingM(false, true, false);
        } else AdminView.tournament_matchmakingM(false, false, false);
    }

    public static void tournamentMatchmakingA(String name){
        if (getTournament(name)!=null && getTournament(name).getStartDate().lowerThan(new Date())){
            AdminView.tournament_matchmakingA(getTournament(name).getMatchmaking().randomMatchmake(getTournament(name).getRandomizedParticipants(), getTournament(name)), true);
        } else AdminView.tournament_matchmakingA(false, false);
    }

    public static void tournament_list(String type){
        HashMap<Tournament, ArrayList<Participant>> result = new HashMap<>();
        if (!getTournaments().isEmpty()){
            if (type == null) {
                ArrayList<Tournament> tournamentsAux = new ArrayList<>();
                for (int i = 0; i < getTournaments().size(); i++) tournamentsAux.add(getTournaments().get(i));
                for (int i = 0; i < getTournaments().size(); i++) {
                    int random = (int) (Math.random() * tournamentsAux.size());
                    Tournament rando = tournamentsAux.get(random);
                    ArrayList<Participant> randomizedParticipants = rando.getRandomizedParticipants();
                    result.put(rando,randomizedParticipants);
                    tournamentsAux.remove(random);
                }
            } else {
                if (type.equals("ADMIN")) {
                    TournamentsController.deletePastTournaments();
                }
                for (int i = 0; i < getTournaments().size(); i++) {
                    ArrayList<Participant> rankedParticipants = getTournaments().get(i).getParticipantsRanked();
                    result.put(tournaments.get(i), rankedParticipants);
                }
            }
        }
        PublicView.tournamentList(result);}

    public static void tournamentAdd(String tournament, String team){
        if (getTournament(tournament)!=null){
            if (getTournament(tournament).getStartDate().greaterThan(new Date())){
                if (team!=null){
                    if (TeamsController.getTeam(team)!=null && TeamsController.isInTeam(Authentication.getCurrentUser().getUsername())!=null){
                        if (TeamsController.isInTeam(Authentication.getCurrentUser().getUsername()).equals(TeamsController.getTeam(team))){
                            if(getTournament(tournament).getParticipant((Player) Authentication.getCurrentUser())==null){
                                if (getTournament(tournament).getParticipant(TeamsController.getTeam(team))==null){
                                    PlayerView.tournament_add(false, false, false, getTournament(tournament).addParticipant(TeamsController.getTeam(team)), true);
                                } else PlayerView.tournament_add(true, false, false, false, true);
                            } else PlayerView.tournament_add(true, false, false, false, true);
                        } else PlayerView.tournament_add(false, true, false, false, true);
                    } else PlayerView.tournament_add(false, true, false, false, true);
                } else {
                    if (getTournament(tournament).getParticipant((Player) Authentication.getCurrentUser())==null){
                        if (TeamsController.isInTeam(Authentication.getCurrentUser().getUsername())!=null){
                            if (getTournament(tournament).getParticipant(TeamsController.isInTeam(Authentication.getCurrentUser().getUsername()))==null){
                                PlayerView.tournament_add(false, false, false, getTournament(tournament).addParticipant((Player) Authentication.getCurrentUser()), false);
                            } else PlayerView.tournament_add(true, false, false, false, false);
                        } else PlayerView.tournament_add(false, false, false, getTournament(tournament).addParticipant((Player) Authentication.getCurrentUser()), false);
                    } else PlayerView.tournament_add(true, false, false, false, false);
                }
            } else PlayerView.tournament_add(false, false, true, false, false);
        } else PlayerView.tournament_add(false, false, false, false, false);
    }

    public static void tournamentRemove(String tournament, String team){
        if (getTournament(tournament)!=null){
            if (team!=null){
                if (TeamsController.getTeam(team)!=null){
                    if (TeamsController.isInTeam(Authentication.getCurrentUser().getUsername()).equals(TeamsController.getTeam(team))){
                        if (getTournament(tournament).getParticipant(TeamsController.getTeam(team))!=null){
                            PlayerView.tournament_remove(false, false, getTournament(tournament).removeParticipant(TeamsController.getTeam(team)), true);
                        } else PlayerView.tournament_remove(true, false, false, true);
                    } else PlayerView.tournament_remove(false, true, false, true);
                } else PlayerView.tournament_remove(false, true, false, true);
            } else {
                if (getTournament(tournament).getParticipant((Player) Authentication.getCurrentUser())!=null){
                    PlayerView.tournament_remove(false, false, getTournament(tournament).removeParticipant((Player) Authentication.getCurrentUser()), false);
                } else PlayerView.tournament_remove(true, false, false, false);
            }
        } else PlayerView.tournament_remove(false, false, false, false);
    }

    public static ArrayList<Tournament> getTournaments() {
        return tournaments;
    }
    public static void addTournament(Tournament tournament){
        tournaments.add(tournament);
    }
}