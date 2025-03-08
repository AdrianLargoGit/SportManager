package upm.etsisi.poo.view;

import upm.etsisi.poo.model.Matchmaking;

import java.util.ArrayList;
import java.util.List;

public class AdminView {


    public static void player_create(boolean success){
        if (success){
            System.out.println("\nJUGADOR CREADO CON EXITO");
        } else System.out.println("\nAsegurate de que no existe ningun equipo o jugador con ese nombre (username)");
    }


    public static void player_delete(boolean team, boolean isParticipant, boolean success){
        if (success){
            System.out.println("\nJUGADOR ELIMINADO CON EXITO");
        } else if (team){
            System.out.println("\nAsegurate de que el equipo del jugador no este participando");
        } else if (isParticipant){
            System.out.println("\nAsegurate de que el jugador no este participando");
        } else System.out.println("\nAsegurate de que el jugador exista");
    }


    public static void add_points(boolean success){
        if (success){
            System.out.println("\nPUNTOS AÑADIDOS CORRECTAMENTE");
        } else System.out.println("\nAsegurate de que el jugador existe y la categoria tambien");
    }


    public static void team_create(boolean success){
        if (success){
            System.out.println("\nEQUIPO CREADO CON EXITO");
        } else System.out.println("\nAsegurate de que no existe ningun equipo o jugador con ese nombre");
    }


    public static void team_delete(boolean isParticipant, boolean success){
        if (success){
            System.out.println("\nEQUIPO ELIMINADO CON EXITO");
        } else if (isParticipant) {
            System.out.println("\nAsegurate de que el equipo no este participando");
        } else System.out.println("\nAsegurate de que el equipo exista");
    }


    public static void team_add(boolean isInT, boolean team, boolean success){
        if (success){
            System.out.println("\nJUGADOR AÑADIDO AL EQUIPO CON EXITO");
        } else if (team) {
            System.out.println("\nAsegurate de que el equipo existe");
        } else if (isInT) {
            System.out.println("\nAsegurate de que el jugador no se encuentra en un equipo");
        } else System.out.println("\nAsegurate de que el jugador existe");
    }


    public static void team_remove(boolean team, boolean isNot, boolean success){
        if (success){
            System.out.println("\nJUGADOR ELIMINADO DEL EQUIPO CON EXITO");
        } else if (isNot) {
            System.out.println("\nAsegurate de que el jugador pertenece al equipo");
        } else if (team){
            System.out.println("\nAsegurate de que el equipo existe");
        } else System.out.println("\nAsegurate de que el jugador existe");
    }


    public static void tournament_create(boolean dates, boolean datesCategory, boolean success){
        if (success){
            System.out.println("\nTORNEO CREADO CON EXITO");
        } else if (datesCategory) {
            System.out.println("\nAsegurate de que la fecha de inicio es anterior a la de finalizacion y de que la categoria exista");
        } else if (dates) {
            System.out.println("\nAsegurate de que las fechas tienen un formato correcto");
        } else System.out.println("\nAsegurate de que no existe otro torneo con el mismo nombre");
    }
    public static void tournament_delete(boolean success){
        if (success){
            System.out.println("\nTORNEO BORRADO CORRECTAMENTE");
        } else System.out.println("\nAsegurate de que exista el torneo");
    }
    public static void tournament_matchmakingM(boolean notP, boolean notEx, boolean success){
        if(success){
            System.out.println("\nMATCHMAKING MANUAL COMPLETADO");
        } else if (notP) {
            System.out.println("\nAsegurate de que los participantes elegidos esten participando en el torneo");
        } else if (notEx) {
            System.out.println("\nAsegurate de que los participantes elegidos existan");
        } else System.out.println("\nAsegurate de que el torneo existe y esta en curso");
    }
    public static void tournament_matchmakingA(boolean success, boolean participants){
        if (participants){
            if (success){
                System.out.println("\nMATCHMAKING AUTOMATICO COMPLETADO");
            } else System.out.println("\nAsegurate de que el torneo cuenta con un numero optimo de participantes");
        } else System.out.println("\nAsegurate de que el torneo existe y esta en curso");
    }
    public static void listMatchmaking(List<Matchmaking> matchmaking){
        if (matchmaking.isEmpty()){
            System.out.println("\nNo hay emparejamientos.");
        } else {
            for (Matchmaking value : matchmaking) {
                System.out.println(value);
            }
        }
    }
}
