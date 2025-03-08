package upm.etsisi.poo.model;

import jakarta.persistence.*;
import upm.etsisi.poo.controller.MatchmakingController;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matchmakings")
public class Matchmaking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_name")
    private Tournament tournament;
    @Transient
    private ArrayList<Participant> participants = new ArrayList<>();
    @ManyToMany(mappedBy = "matchmakings")
    private List<Player> players = new ArrayList<>();
    @ManyToMany(mappedBy = "matchmakings")
    private List<Team> teams = new ArrayList<>();

    public Matchmaking(){
    }

    public Matchmaking(Participant participant1, Participant participant2) {
        participants.add(participant1);
        participant1.addMatch(this);
        if (participant1 instanceof Player){
            players.add((Player) participant1);
        } else teams.add((Team) participant1);
        participants.add(participant2);
        participant2.addMatch(this);
        if (participant2 instanceof Player){
            players.add((Player) participant2);
        } else teams.add((Team) participant2);
    }

    public boolean isMatchmaked(Participant participant) {
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).equals(participant)) {
                return true;
            }
        }
        return false;
    }

    public void initializateParticipant(){
        this.participants.addAll(players);
        this.participants.addAll(teams);
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public String toString() {
        return participants.get(0) + " vs " + participants.get(1);
    }
}
