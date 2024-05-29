package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndgameScreenController {
    @FXML
    public StackPane root;
    @FXML
    public Text winnerText;
    @FXML
    public VBox playerCol;
    @FXML
    public VBox gamePointsCol;
    @FXML
    public VBox objectivePointsCol;
    @FXML
    public VBox totalPointsCol;

    private GUIState guiState;

    private Map<String, Integer> gamePoints;
    private Map<String, Integer> objectivePoints;
    private Map<String, Integer> totalPoints;

    @FXML
    public void initialize() {
        registerEvents();

        guiState = CodexGUI.getGUI().getGuiState();

        gamePoints = new HashMap<>();
        objectivePoints = new HashMap<>();
        totalPoints = new HashMap<>();
    }

    private void evaluate() {
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

        winnerText.setText(sortedUsernames.getFirst());

        for (String username : sortedUsernames) {
            Text playerText = new Text(username);
            Text gamePointsText = new Text(gamePoints.get(username).toString());
            Text objectivePointsText = new Text(objectivePoints.get(username).toString());
            Text totalPointsText = new Text(totalPoints.get(username).toString());

            playerText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            gamePointsText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            objectivePointsText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            totalPointsText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);

            playerText.getStyleClass().add("endgame-text");
            gamePointsText.getStyleClass().add("endgame-text");
            objectivePointsText.getStyleClass().add("endgame-text");
            totalPointsText.getStyleClass().add("endgame-text");
            totalPointsText.getStyleClass().add("total");

            StackPane playerPane = new StackPane();
            StackPane gamePointsPane = new StackPane();
            StackPane objectivePointsPane = new StackPane();
            StackPane totalPointsPane = new StackPane();

            playerPane.getChildren().add(playerText);
            gamePointsPane.getChildren().add(gamePointsText);
            objectivePointsPane.getChildren().add(objectivePointsText);
            totalPointsPane.getChildren().add(totalPointsText);

            playerPane.getStyleClass().add("endgame-cell");
            gamePointsPane.getStyleClass().add("endgame-cell");
            objectivePointsPane.getStyleClass().add("endgame-cell");
            totalPointsPane.getStyleClass().add("endgame-cell");

            playerCol.getChildren().add(playerPane);
            gamePointsCol.getChildren().add(gamePointsPane);
            objectivePointsCol.getChildren().add(objectivePointsPane);
            totalPointsCol.getChildren().add(totalPointsPane);
        }
    }

    public void home(ActionEvent ignored) {
        //TODO should we call something on the server?
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    public void showError(String errorMessage) {
        //TODO implement
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> {
            showError(errorEvent.getErrorMsg());
            errorEvent.consume();
        });

        root.addEventFilter(GUIEventTypes.SET_OBJECTIVE_POINTS_EVENT, e -> {
            String username = e.getUsername();
            int objPoints = e.getObjectivePoints();

            gamePoints.put(username, guiState.getGamePoints(username));
            objectivePoints.put(username, objPoints);
            totalPoints.put(username, gamePoints.get(username) + objectivePoints.get(username));

            if (objectivePoints.keySet().size() == guiState.getNumPlayers()) {
                evaluate();
            }
        });
    }
}
