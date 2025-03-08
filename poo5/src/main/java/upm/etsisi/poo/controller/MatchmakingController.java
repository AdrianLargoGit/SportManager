package upm.etsisi.poo.controller;

import jakarta.persistence.*;
import upm.etsisi.poo.model.Matchmaking;
import upm.etsisi.poo.model.Participant;
import upm.etsisi.poo.model.Tournament;
import upm.etsisi.poo.view.AdminView;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MatchmakingController {
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Matchmaking> matchmaking;

    public MatchmakingController() {
        this.matchmaking = new ArrayList<>();
    }

    public boolean createMatchmake(Participant participant1, Participant participant2, Tournament tournament) {
        if (!isMatchmaked(participant1) && !isMatchmaked(participant2)) {
            Matchmaking matchmaking1 = new Matchmaking(participant1, participant2);
            matchmaking1.setTournament(tournament);
            matchmaking.add(matchmaking1);
            getMatchmakings();
            return true;
        }
        return false;
    }

    public boolean randomMatchmake(ArrayList<Participant> participants, Tournament tournament) {
        boolean result = false;
        if (participants.size() % 2 == 0 && matchmaking.size() != participants.size() / 2) {
            for (int i = 0; i < participants.size(); i++) {
                if (!isMatchmaked(participants.get(i))) {
                    if (i + 1 < participants.size() && !isMatchmaked(participants.get(i + 1))) {
                        Matchmaking matchmaking1 = new Matchmaking(participants.get(i), participants.get(i + 1));
                        matchmaking1.setTournament(tournament);
                        matchmaking.add(matchmaking1);
                        i++;
                    }
                }
            }
            getMatchmakings();
            result = true;
            for (int i = 0; i < participants.size(); i++) {
                if (!isMatchmaked(participants.get(i))) {
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean isMatchmaked(Participant participant) {
        for (int i = 0; i < matchmaking.size(); i++) {
            if (matchmaking.get(i).isMatchmaked(participant)) {
                return true;
            }
        }
        return false;
    }

    public void removeMatchmaking(Participant participant) {
        int i = 0;
        while (i < matchmaking.size()) {
            if (matchmaking.get(i).isMatchmaked(participant)) {
                matchmaking.remove(i);
            }
            i++;
        }
    }

    public Matchmaking getMatch(Participant participant){
        int i = 0;
        while (i < matchmaking.size()) {
            if (matchmaking.get(i).isMatchmaked(participant)) {
                return matchmaking.get(i);
            }
            i++;
        }
        return null;
    }

    public List<Matchmaking> getMatchmaking() {
        return matchmaking;
    }

    public void setMatchmaking(List<Matchmaking> matchmaking) {
        this.matchmaking = matchmaking;
    }

    public void getMatchmakings(){
        AdminView.listMatchmaking(this.matchmaking);
    }
}
