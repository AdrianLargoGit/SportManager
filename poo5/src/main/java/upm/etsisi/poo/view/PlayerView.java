package upm.etsisi.poo.view;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import upm.etsisi.poo.model.Stat;

import java.util.List;

public class PlayerView {
    public static void tournament_add(boolean isP, boolean notT, boolean notC, boolean success, boolean team){
        if (success){
            if (team){
                System.out.println("\nTU EQUIPO HA SIDO AÑADIDO AL TORNEO");
            } else System.out.println("\nHAS SIDO AÑADIDO AL TORNEO");
        } else if (isP) {
            System.out.println("\nAsegurate de que no participas ni tu ni tu equipo, si tienes, en el torneo");
        } else if (notT) {
            System.out.println("\nAsegurate de que perteneces al equipo y de que este existe");
        } else if (notC) {
            System.out.println("\nAsegurate de que el torneo aun no esta en curso");
        } else System.out.println("\nAsegurate de que existe el torneo");
    }
    public static void tournament_remove(boolean notP, boolean notT, boolean success, boolean team){
        if (success){
            if (team){
                System.out.println("\nTU EQUIPO HA SIDO ELIMINADO DEL TORNEO");
            } else System.out.println("\nHAS SIDO ELIMINADO DEL TORNEO");
        } else if (notP) {
            System.out.println("\nAsegurate de que participas, tu o tu equipo, en el torneo");
        } else if (notT) {
            System.out.println("\nAsegurate de que perteneces al equipo y de que este existe");
        } else System.out.println("\nAsegurate de existe el torneo");
    }
    public static void statistics_showJson(List<Stat> stats){
        JsonObject jsonObject = new JsonObject();
        for (int i = 0; i< stats.size(); i++){
            jsonObject.addProperty(stats.get(i).getCategory().name(), stats.get(i).getValue());
        }
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        System.out.println("JSON GENERADO: ");
        System.out.println(json);
    }
    public static void statistics_showCSV(List<Stat> stats){
        for (int i = 0; i<stats.size(); i++){
            StringBuilder csv = new StringBuilder();
            csv.append(stats.get(i).getCategory()).append(";").append(stats.get(i).getValue());
            System.out.println(csv);
        }
    }
}
