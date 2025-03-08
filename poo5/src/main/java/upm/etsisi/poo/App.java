package upm.etsisi.poo;
import upm.etsisi.poo.controller.ComandController;

public class App {
    public static void main(String[] args) {
        ComandController.start();
    }
    //Borra la linea 112 de TournamentsController el codigo:
    // && getTournament(name).getStartDate().lowerThan(new Date())
    // para que te deje añadir matchmaking automaticos sin que haya empezado el torneo

    /*login adrian@alumnos.upm.es;1432 (crea este administrador en la base de datos como ejemplo)
    player-create adri@alumnos.upm.es;123;adri;largo;87654321J
    player-create adria@alumnos.upm.es;123;adrian;largo;12345679F
    save-changes
    team-create god
    save-changes
    team-add adri@alumnos.upm.es;god
    save-changes
    tournament-create futbol;14/01/2025;15/01/2026;liga1;futbol7;money generated
    add-points adri@alumnos.upm.es;money generated;12
    save-changes
    logout
    login adri@alumnos.upm.es;123
    tournament-add futbol;god
    tournament-add futbol
    save-changes
    logout
    login adria@alumnos.upm.es;123
    tournament-add futbol;god
    tournament-add futbol
    save-changes
    login adrian@alumnos.upm.es;1432
    tournament-delete futbol
    save-changes
     */
}
