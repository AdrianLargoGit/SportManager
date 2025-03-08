package upm.etsisi.poo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public enum Categories {
    SCORED_POINTS, ASSIST_POINTS, WON_GAMES, WON_TOURNAMENTS, MONEY_GENERATED;

    public static Categories getCategory(String category) {
        Categories categoryS = null;
        if (category!=null){
            category = category.toUpperCase();
            if (category.contains(" ")) {
                String[] arg = category.split(" ");
                category = arg[0] + "_" + arg[1];
            }
            for (Categories categoryA : Categories.values()) {
                if (categoryA.name().equals(category)) {
                    categoryS = categoryA;
                }
            }
        }
        return categoryS;
    }

    public static Categories[] getCategories() {
        return Categories.values();
    }

}
