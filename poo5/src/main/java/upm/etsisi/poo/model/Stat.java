package upm.etsisi.poo.model;

import jakarta.persistence.*;
import upm.etsisi.poo.controller.PlayersController;
import upm.etsisi.poo.controller.TeamsController;

@Entity
@Table(name = "stats")
public class Stat {
    private static final String ATTR_CATEGORY_NAME = "category";
    private static final String ATTR_VALUE_NAME = "value";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Categories category;
    @Column(name = "value", nullable = false)
    private double value;
    @ManyToOne
    @JoinColumn(name = "player_username", referencedColumnName = "username")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_name", referencedColumnName = "name")
    private Team team;

    public Stat(){
    }

    public Stat(String category) {
        this.category = Categories.getCategory(category);
        this.value = 0.0;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = Categories.getCategory(category);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return getCategory() + ": " + getValue();
    }
}