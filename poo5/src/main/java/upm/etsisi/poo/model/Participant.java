package upm.etsisi.poo.model;


import java.util.List;

public interface Participant {
    void deleteMatch(Matchmaking matchmaking);
    void deleteTournament(Tournament tournament);
    void addMatch(Matchmaking matchmaking);
    void addTournament(Tournament tournament);
    List<Matchmaking> getMatchmakings();
    String getName();
    Stat getStat(String category);
}
