package upm.etsisi.poo.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import upm.etsisi.poo.model.*;
import upm.etsisi.poo.view.PublicView;

import java.util.ArrayList;
import java.util.List;

public class DataController {
    private static Session session;
    public static boolean initialitation(){
        Authentication.getInstance();
        AdminsController.getInstance();
        TournamentsController.getInstance();
        TeamsController.getInstance();
        PlayersController.getInstance();
        try {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();;
            session = sessionFactory.openSession();
            return true;
        } catch (Exception es){
            System.out.println(es.getMessage());
            return false;
        }
    }

    public static boolean getData(){
        try{
            List<Admin> admins = session.createQuery("FROM Admin a", Admin.class).getResultList();
            for (Admin admin : admins){
                AdminsController.addAdmin(admin);
            }
            List<Team> teams = session.createQuery("SELECT DISTINCT t FROM Team t JOIN FETCH t.stats", Team.class).getResultList();
            for (Team team : teams){
                TeamsController.addTeam(team);
            }
            List<Player> players = session.createQuery("SELECT DISTINCT p FROM Player p JOIN FETCH p.stats", Player.class).getResultList();
            for (Player player : players){
                PlayersController.addPlayer(player);
                if (player.getTeam()!=null){
                    TeamsController.getTeam(player.getTeam().getName()).addPlayer(player);
                }
            }
            List<Tournament> tournaments = session.createQuery("FROM Tournament t", Tournament.class).getResultList();
            for (Tournament tournament : tournaments){
                ArrayList<Participant> participants = new ArrayList<>();
                List<Player> players1 = session.createQuery("SELECT p FROM Player p JOIN p.tournaments t WHERE t.name = :tournamentName", Player.class)
                        .setParameter("tournamentName", tournament.getName()).getResultList();
                List<Team> teams1 = session.createQuery("SELECT te FROM Team te JOIN te.tournaments t WHERE t.name = :tournamentName", Team.class)
                        .setParameter("tournamentName", tournament.getName()).getResultList();
                participants.addAll(players1);
                participants.addAll(teams1);
                tournament.setParticipants(participants);
                for (Matchmaking matchmaking : tournament.getMatchmaking().getMatchmaking()){
                    matchmaking.initializateParticipant();
                }
                TournamentsController.addTournament(tournament);
            }
            return true;
        } catch (Exception es){
            System.out.println(es.getMessage());
            return false;
        }
    }

    public static void saveData() {
        try {
            session.beginTransaction();
            List<Player> currentPlayers = session.createQuery("FROM Player", Player.class).getResultList();
            for (Player currentPlayer : currentPlayers) {
                if (!PlayersController.getPlayers().contains(currentPlayer)) {
                    session.delete(currentPlayer);
                }
            }
            session.getTransaction().commit();
            session.beginTransaction();
            List<Team> currentTeams = session.createQuery("FROM Team", Team.class).getResultList();
            for (Team currentTeam : currentTeams) {
                if (!TeamsController.getTeams().contains(currentTeam)) {
                    session.delete(currentTeam);
                }
            }
            session.getTransaction().commit();
            session.beginTransaction();
            List<Tournament> currentTournaments = session.createQuery("FROM Tournament", Tournament.class).getResultList();
            for (Tournament currentTournament : currentTournaments) {
                if (!TournamentsController.getTournaments().contains(currentTournament)) {
                    session.delete(currentTournament);
                }
            }
            session.getTransaction().commit();
            session.beginTransaction();
            for (Admin admin : AdminsController.getAdmins()) {
                session.persist(admin);
            }
            session.getTransaction().commit();
            session.beginTransaction();
            for (Team team : TeamsController.getTeams()) {
                for (Stat stat : team.getStats()) {
                    stat.setTeam(team);
                    session.persist(stat);
                }
                session.persist(team);
            }
            session.getTransaction().commit();
            session.beginTransaction();
            for (Player player : PlayersController.getPlayers()) {
                for (Team team : TeamsController.getTeams()) {
                    if (team.getPlayers().contains(player)){
                        player.setTeam(team);
                    }
                }
                for (Stat stat : player.getStats()) {
                    stat.setPlayer(player);
                    session.persist(stat);
                }
                session.persist(player);
            }
            session.getTransaction().commit();
            session.beginTransaction();
            for (Tournament tournament : TournamentsController.getTournaments()) {
                session.persist(tournament.getStartDate());
                session.persist(tournament.getEndDate());
                session.persist(tournament);
            }
            session.getTransaction().commit();
            PublicView.saveData(true);
        } catch (Exception es) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            PublicView.otherErrors("\nMuchos datos en proceso. Intenta guardarlos de uno en uno para facilitar el trabajo.");
            PublicView.saveData(false);
        }
    }

    public static void close(){
        session.close();
    }
}
