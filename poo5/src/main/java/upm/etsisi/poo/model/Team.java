package upm.etsisi.poo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team implements Participant {
    private static final String ATTR_NAME_NAME = "name";
    @Id
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "team")
    private List<Player> players = new ArrayList<>();
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Stat> stats = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "creator_username", nullable = false)
    private Admin creator;

    @ManyToMany
    @JoinTable(
            name = "teams_tournament",
            joinColumns = @JoinColumn(name = "name"),
            inverseJoinColumns = @JoinColumn(name = "tournament_name")
    )
    private List<Tournament> tournaments = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "matchmaking_teams",
            joinColumns = @JoinColumn(name = "name"),
            inverseJoinColumns = @JoinColumn(name = "matchmaking_id")
    )
    private List<Matchmaking> matchmakings = new ArrayList<>();

    public Team(){

    }

    public Team(String name, Admin creator) throws ModelException {
        Validations.isNotNull(ATTR_NAME_NAME, name);
        Validations.isMinimum(ATTR_NAME_NAME, name, 2);
        this.name = name;
        this.players = new ArrayList<>();
        this.stats = initialStats();
        this.creator = creator;
    }

    private ArrayList<Stat> initialStats() {
        ArrayList<Stat> statList = new ArrayList<>();
        for (int i = 0; i < Categories.getCategories().length; i++) {
            Stat stat = new Stat(Categories.getCategories()[i].name());
            statList.add(stat);
        }
        return statList;
    }

    @Override
    public void addMatch(Matchmaking matchmaking){
        matchmakings.add(matchmaking);
    }
    @Override
    public void deleteMatch(Matchmaking matchmaking){
        this.matchmakings.remove(matchmaking);
    }
    @Override
    public void deleteTournament(Tournament tournament){
        this.tournaments.remove(tournament);
    }

    @Override
    public Stat getStat(String category) {
        updateStats();
        Stat stat = null;
        for (int i = 0; i < stats.size(); i++) {
            if (stats.get(i).getCategory().equals(Categories.getCategory(category))) {
                stat = stats.get(i);
            }
        }
        return stat;
    }

    public void updateStats() {
        for (int i = 0; i < stats.size(); i++) {
            stats.get(i).setValue(0);
        }
        for (int i = 0; i < players.size(); i++) {
            for (int d = 0; d < stats.size(); d++) {
                if (stats.get(d).getValue()==0){
                    stats.get(d).setValue(players.get(i).getStats().get(d).getValue());
                } else stats.get(d).setValue(stats.get(d).getValue() * players.get(i).getStats().get(d).getValue());
            }
        }
        for (int i = 0; i < stats.size(); i++) {
            stats.get(i).setValue(Math.pow(stats.get(i).getValue(), 1.0 / players.size()));
        }
    }

    public List<Stat> getStats() {
        updateStats();
        return stats;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer(String username) {
        Player result = null;
        int i = 0;
        while (i < players.size() && result == null) {
            if (players.get(i).getUsername().equalsIgnoreCase(username)) result = players.get(i);
            i++;
        }
        return result;
    }

    public boolean addPlayer(Player player) {
        boolean result = false;
        if (player == null) {
            return false;
        }
        if (getPlayer(player.getUsername()) == null) {
            players.add(player);
            updateStats();
            result = true;
        }
        return result;
    }

    public boolean removePlayer(String username) {
        boolean result = false;
        if (getPlayer(username) != null) {
            players.remove(getPlayer(username));
            updateStats();
            result = true;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public List<Matchmaking> getMatchmakings() {
        return matchmakings;
    }

    @Override
    public void addTournament(Tournament tournament){
        tournaments.add(tournament);
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setStats(ArrayList<Stat> stats) {
        this.stats = stats;
    }
}
