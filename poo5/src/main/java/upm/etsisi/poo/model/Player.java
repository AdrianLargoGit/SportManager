package upm.etsisi.poo.model;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player extends User implements Participant{
    private static final String ATTR_USERNAME_NAME = "username";
    private static final String ATTR_PASSWORD_NAME = "password";
    private static final String ATTR_NAME_NAME = "name";
    private static final String ATTR_SURNAME_NAME = "surname";
    private static final String ATTR_DNI_NAME = "dni";
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "dni", unique = true, nullable = false)
    private String dni;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Stat> stats = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "creator_username", nullable = false)
    private Admin creator;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "team_name", referencedColumnName = "name")
    private Team team;

    @ManyToMany
    @JoinTable(
            name = "players_tournament",
            joinColumns = @JoinColumn(name = "name", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "tournament_name")
    )
    private List<Tournament> tournaments = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "matchmaking_players",
            joinColumns = @JoinColumn(name = "name", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "matchmaking_id")
    )
    private List<Matchmaking> matchmakings = new ArrayList<>();

    public Player(){
    }

    public Player(String username, String password, String name, String surname, String dni, Admin creator) throws ModelException {
        Validations.isNotNull(ATTR_USERNAME_NAME, username);
        usernameValidate(username);
        Validations.isNotNull(ATTR_PASSWORD_NAME, password);
        Validations.isNotNull(ATTR_NAME_NAME, name);
        Validations.isString(ATTR_NAME_NAME, name);
        Validations.isNotNull(ATTR_SURNAME_NAME, surname);
        Validations.isString(ATTR_SURNAME_NAME, surname);
        Validations.isNotNull(ATTR_DNI_NAME, dni);
        Validations.isMinimum(ATTR_PASSWORD_NAME, password, 3);
        Validations.isMinimum(ATTR_NAME_NAME, name, 3);
        Validations.isMinimum(ATTR_DNI_NAME, dni, 9);
        Validations.isMaximum(ATTR_DNI_NAME, dni, 9);
        dniValidation(dni);
        setUsername(username);
        setPassword(password);
        this.name = name;
        this.surname = surname;
        this.dni = dni;
        this.stats = initialStats();
        this.creator = creator;
    }

    private void dniValidation(String dni) throws ModelException{
        String[] dnis = dni.split("");
        boolean correct = true;
        if (dnis[8].matches("[a-zA-Z]")){
            for (int i = 0; i< dnis.length-1; i++){
                if (!dnis[i].matches("\\d")){
                    correct = false;
                }
            }
        } else correct = false;
        if (!correct){
            throw new ModelException("Formato del DNI incorrecto.");
        }
    }

    private ArrayList<Stat> initialStats(){
        ArrayList<Stat> statList = new ArrayList<>();
        for (int i = 0; i< Categories.getCategories().length; i++){
            Stat stat = new Stat(Categories.getCategories()[i].name());
            statList.add(stat);
        }
        return statList;
    }

    @Override
    public Stat getStat(String category){
        Stat stat = null;
        for (int i = 0; i<stats.size(); i++){
            if (stats.get(i).getCategory().equals(Categories.getCategory(category))){
                stat = stats.get(i);
            }
        }
        return stat;
    }


    public boolean updateStat(String category, double value){
        boolean updated = false;
        Stat stat = getStat(category);
        if (stat!=null){
            stat.setValue(stat.getValue()+value);
            updated = true;
        }
        return updated;
    }

    public String getName() {
        return getUsername();
    }

    public String getName1(){
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDni() {
        return dni;
    }

    public Admin getCreator() {
        return creator;
    }

    public List<Stat> getStats() {
        return stats;
    }

    @Override
    public void addTournament(Tournament tournament){
        tournaments.add(tournament);
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

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    public Team getTeam() {
        return team;
    }

    public List<Matchmaking> getMatchmakings() {
        return matchmakings;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return getSurname() + ", " + getName1() + "(" + getDni() + ")";
    }
}
