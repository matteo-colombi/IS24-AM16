package it.polimi.ingsw.am16.client.view.gui;

public class GUIState {

    private String username;
    private String gameId;

    private WelcomeScreenController welcomeScreenController;
    private GamesScreenController gamesScreenController;

    public synchronized WelcomeScreenController getWelcomeScreenController() {
        return welcomeScreenController;
    }

    public synchronized void setWelcomeScreenController(WelcomeScreenController welcomeScreenController) {
        this.welcomeScreenController = welcomeScreenController;
    }

    public synchronized GamesScreenController getGamesScreenController() {
        return gamesScreenController;
    }

    public synchronized void setGamesScreenController(GamesScreenController gamesScreenController) {
        this.gamesScreenController = gamesScreenController;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized String getUsername() {
        return username;
    }
}
