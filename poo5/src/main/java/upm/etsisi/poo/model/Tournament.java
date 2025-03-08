package upm.etsisi.poo.model;

import jakarta.persistence.*;
import upm.etsisi.poo.controller.MatchmakingController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class Tournament {
    private static final String ATTR_NAME_NAME = "name";
    private static final String ATTR_LEAGUE_NAME = "league";
    private static final String ATTR_SPORT_NAME = "sport";
    private static final String ATTR_CATEGORYRANK_NAME = "categoryRank";
    @Id
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "startDate", referencedColumnName = "id")
    private Date startDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endDate", referencedColumnName = "id")
    private Date endDate;
    @Column(name = "league")
    private String league;
    @Column(name = "sport")
    private String sport;
    @Enumerated(EnumType.STRING)
    @Column(name = "category_rank")
    private Categories categoryRank;
    @Transient
    private ArrayList<Participant> participants = new ArrayList<>();
    @ManyToMany(mappedBy = "tournaments")
    private List<Player> players = new ArrayList<>();
    @ManyToMany(mappedBy = "tournaments")
    private List<Team> teams = new ArrayList<>();
    @Embedded
    private MatchmakingController matchmaking;

    public Tournament(){}

    public Tournament(String name, Date startDate, Date endDate, String league, String sport, String categoryRank) throws ModelException {
        Validations.isNotNull(ATTR_NAME_NAME, name);
        Validations.isNotNull(ATTR_LEAGUE_NAME, league);
        Validations.isNotNull(ATTR_SPORT_NAME, sport);
        Validations.isNotNull(ATTR_CATEGORYRANK_NAME, categoryRank);
        Validations.isMinimum(ATTR_NAME_NAME, name, 3);
        Validations.isMinimum(ATTR_SPORT_NAME, sport, 2);
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.league = league;
        this.sport = sport;
        this.categoryRank = Categories.getCategory(categoryRank);
        this.matchmaking = new MatchmakingController();
    }

    public Participant getParticipant(Participant participant) {
        Participant result = null;
        int i = 0;
        while (i < participants.size() && result == null) {
            if (participants.get(i).equals(participant)) {
                result = participants.get(i);
            }
            i++;
        }
        return result;
    }

    public boolean addParticipant(Participant participant) {
        if (getParticipant(participant) == null) {
            participant.addTournament(this);
            if (participant instanceof Player){
                players.add((Player) participant);
            } else teams.add((Team) participant);
            participants.add(participant);
            return true;
        } else return false;
    }

    public boolean removeParticipant(Participant participant) {
        boolean result = false;
        if (getParticipant(participant) != null) {
            if (matchmaking.isMatchmaked(participant)) {
                matchmaking.removeMatchmaking(participant);
                participant.deleteMatch(matchmaking.getMatch(participant));
            }
            participant.deleteTournament(this);
            participants.remove(participant);
            result = true;
        }
        return result;
    }

    public ArrayList<Participant> getRandomizedParticipants() {
        ArrayList<Participant> result = new ArrayList<>(), aux = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) aux.add(participants.get(i));
        for (int i = 0; i < participants.size(); i++) {
            int random = (int) (Math.random() * aux.size());
            result.add(aux.get(random));
            aux.remove(aux.get(random));
        }
        return result;
    }

    public ArrayList<Participant> getParticipantsRanked() {
        ArrayList<Participant> result = participants;
        result.sort(Comparator.comparingDouble(obj -> {
            List<Stat> items;
            if (obj instanceof Player) {
                items = ((Player) obj).getStats();
            } else if (obj instanceof Team) {
                items = ((Team) obj).getStats();
            } else {
                return Double.MIN_VALUE; // Valor por defecto si no es una clase válida
            }

            // Buscar el Item con el nombre especificado
            return items.stream()
                    .filter(item -> item.getCategory().equals(categoryRank))
                    .map(Stat::getValue)
                    .findFirst()
                    .orElse(Double.MIN_VALUE); // Valor por defecto si no se encuentra
        }).reversed());
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Categories getCategoryRank() {
        return categoryRank;
    }

    public void setCategoryRank(Categories categoryRank) {
        this.categoryRank = categoryRank;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public MatchmakingController getMatchmaking() {
        return matchmaking;
    }
}