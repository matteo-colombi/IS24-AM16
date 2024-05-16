package it.polimi.ingsw.am16.client.view.gui;

public class GUIState {

    private String username;
    private String gameId;

    private WelcomeScreenController welcomeScreenController;
    private GamesScreenController gamesScreenController;

    public WelcomeScreenController getWelcomeScreenController() {
        return welcomeScreenController;
    }

    public void setWelcomeScreenController(WelcomeScreenController welcomeScreenController) {
        this.welcomeScreenController = welcomeScreenController;
    }

    public GamesScreenController getGamesScreenController() {
        return gamesScreenController;
    }

    public void setGamesScreenController(GamesScreenController gamesScreenController) {
        this.gamesScreenController = gamesScreenController;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
