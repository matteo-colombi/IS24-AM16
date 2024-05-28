package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EndgameScreenController {
    @FXML
    public StackPane root;
    @FXML
    public VBox playerCol;
    @FXML
    public VBox gamePointsCol;
    @FXML
    public VBox objectivePointsCol;
    @FXML
    public VBox totalPointsCol;

    private GUIState guiState;

    @FXML
    public void initialize() {
        registerEvents();
        guiState = CodexGUI.getGUI().getGuiState();

        final Map<String, Integer> gamePoints = new HashMap<>();
        final Map<String, Integer> objectivePoints =new HashMap<>();
        final Map<String, Integer> totalPoints = new HashMap<>();

        for(String username : guiState.getPlayerUsernames()) {
            gamePoints.put(username, guiState.getGamePoints(username));
            objectivePoints.put(username, guiState.getObjectivePoints(username));
            totalPoints.put(username, gamePoints.get(username) + objectivePoints.get(username));
        }

        List<String> sortedUsernames = this.guiState.getPlayerUsernames()
                .stream()
                .sorted((s1, s2) -> {
                            if (totalPoints.get(s1).equals(totalPoints.get(s2))) {
                                return Integer.compare(objectivePoints.getOrDefault(s1, 0), objectivePoints.getOrDefault(s2, 0));
                            } else {
                                return Integer.compare(totalPoints.get(s1), totalPoints.get(s2));
                            }
                        }
                ).toList().reversed();

//        playerCol.getChildren().clear();
//        gamePointsCol.getChildren().clear();
//        objectivePointsCol.getChildren().clear();
//        totalPointsCol.getChildren().clear();

        for(String username : sortedUsernames) {
            StackPane playerPane = new StackPane();
            Text playerText = new Text(username);

            StackPane gamePointsPane = new StackPane();
            Text gamePointsText = new Text(gamePoints.get(username).toString());

            StackPane objectivePointsPane = new StackPane();
            Text objectivePointsText = new Text(objectivePoints.get(username).toString());

            StackPane totalPointsPane = new StackPane();
            Text totalPointsText = new Text(totalPoints.get(username).toString());

            playerPane.getChildren().add(playerText);
            gamePointsPane.getChildren().add(gamePointsText);
            objectivePointsPane.getChildren().add(objectivePointsText);
            totalPointsPane.getChildren().add(totalPointsText);
        }
    }

    public void home(ActionEvent ignored) {
        //TODO implement
    }

    public void showError(String errorMessage) {
        //TODO implement
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> {
            showError(errorEvent.getErrorMsg());
            errorEvent.consume();
        });

        root.addEventFilter(GUIEventTypes.SET_GAMES_LIST_EVENT, gamesListEvent -> {
            gamesListEvent.consume();
        });
    }
}
