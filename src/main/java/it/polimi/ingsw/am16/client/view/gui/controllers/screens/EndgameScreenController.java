package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.client.view.gui.events.ErrorEvent;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.ErrorFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIAssetRegistry;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for the end game screen. The end game screen displays the amount of points for each player as well as the name of the winner(s) of the game.
 */
public class EndgameScreenController {

    @FXML
    public StackPane root;
    @FXML
    public Text winnersTitleText;
    @FXML
    public Text winnersText;
    @FXML
    public VBox playerCol;
    @FXML
    public VBox gamePointsCol;
    @FXML
    public VBox objectivePointsCol;
    @FXML
    public VBox totalPointsCol;
    @FXML
    public VBox infoCol;
    @FXML
    public StackPane playerInfo;
    @FXML
    public Text usernameField;
    @FXML
    public ImageView personalObjective;
    @FXML
    public ImageView commonObjective0;
    @FXML
    public ImageView commonObjective1;
    @FXML
    public StackPane playAreaPane;

    private GUIState guiState;

    private Map<String, Integer> gamePoints;
    private Map<String, Integer> objectivePoints;
    private Map<String, Integer> totalPoints;

    private Map<String, ObjectiveCard> personalObjectives;

    @FXML
    public void initialize() {
        registerEvents();

        guiState = CodexGUI.getGUI().getGuiState();

        gamePoints = new HashMap<>();
        objectivePoints = new HashMap<>();
        totalPoints = new HashMap<>();
    }

    private void setWinners(List<String> winnerUsernames) {
        String winners = String.join(", ", winnerUsernames);

        if (winnerUsernames.size() == 1) {
            winnersTitleText.setText("The winner is:");
        } else {
            winnersTitleText.setText("The winners are:");
        }

        winnersText.setText(winners);
    }

    private void setPersonalObjectives(Map<String, ObjectiveCard> personalObjectives) {
        this.personalObjectives = personalObjectives;
    }

    private void setPoints(String username, int objPoints) {
        gamePoints.put(username, guiState.getGamePoints(username));
        objectivePoints.put(username, objPoints);
        totalPoints.put(username, gamePoints.get(username) + objectivePoints.get(username));

        if (objectivePoints.keySet().size() == guiState.getNumPlayers()) {
            evaluate();
        }
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

        for (String username : sortedUsernames) {
            Text playerText = new Text(username);
            Text gamePointsText = new Text(gamePoints.get(username).toString());
            Text objectivePointsText = new Text(objectivePoints.get(username).toString());
            Text totalPointsText = new Text(totalPoints.get(username).toString());
            Button infoButton = new Button(username);
            ImageView infoImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(FilePaths.GUI_ICONS + "/info.png"))));

            playerText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            gamePointsText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            objectivePointsText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
            totalPointsText.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);

            infoButton.setPrefWidth(45);
            infoButton.setPrefHeight(45);
            infoButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            infoButton.setOnMousePressed(e -> showPlayerInfo(username));

            infoImage.setFitWidth(24);
            infoImage.setFitHeight(24);
            infoImage.setMouseTransparent(true);

            playerText.getStyleClass().add("endgame-text");
            gamePointsText.getStyleClass().add("endgame-text");
            objectivePointsText.getStyleClass().add("endgame-text");
            totalPointsText.getStyleClass().add("endgame-text");
            totalPointsText.getStyleClass().add("total");
            infoButton.getStyleClass().add("transparent");

            StackPane playerPane = new StackPane();
            StackPane gamePointsPane = new StackPane();
            StackPane objectivePointsPane = new StackPane();
            StackPane totalPointsPane = new StackPane();
            StackPane infoPane = new StackPane();

            playerPane.getChildren().add(playerText);
            gamePointsPane.getChildren().add(gamePointsText);
            objectivePointsPane.getChildren().add(objectivePointsText);
            totalPointsPane.getChildren().add(totalPointsText);
            infoPane.getChildren().add(infoButton);
            infoPane.getChildren().add(infoImage);

            playerPane.getStyleClass().add("endgame-cell");
            gamePointsPane.getStyleClass().add("endgame-cell");
            objectivePointsPane.getStyleClass().add("endgame-cell");
            totalPointsPane.getStyleClass().add("endgame-cell");
            if (!username.equals(sortedUsernames.getFirst()))
                infoPane.getStyleClass().add("endgame-cell");

            playerCol.getChildren().add(playerPane);
            gamePointsCol.getChildren().add(gamePointsPane);
            objectivePointsCol.getChildren().add(objectivePointsPane);
            totalPointsCol.getChildren().add(totalPointsPane);
            infoCol.getChildren().add(infoPane);
        }
    }

    private void showPlayerInfo(String username) {
        usernameField.setText(username);

        String personalObjectivePath = GUIAssetRegistry.getAssetName(this.personalObjectives.get(username).getName(), SideType.FRONT);
        String commonObjectivePath0 = GUIAssetRegistry.getAssetName(guiState.getCommonObjectives().getFirst().getName(), SideType.FRONT);
        String commonObjectivePath1 = GUIAssetRegistry.getAssetName(guiState.getCommonObjectives().getLast().getName(), SideType.FRONT);

        Image personalObjectiveImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(personalObjectivePath)), personalObjective.getFitWidth() * 1.3, personalObjective.getFitHeight() * 1.3, true, true);
        Image commonObjectiveImage0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(commonObjectivePath0)), personalObjective.getFitWidth() * 1.3, personalObjective.getFitHeight() * 1.3, true, true);
        Image commonObjectiveImage1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(commonObjectivePath1)), personalObjective.getFitWidth() * 1.3, personalObjective.getFitHeight() * 1.3, true, true);

        personalObjective.setImage(personalObjectiveImage);
        commonObjective0.setImage(commonObjectiveImage0);
        commonObjective1.setImage(commonObjectiveImage1);

        if (!playAreaPane.getChildren().isEmpty())
            playAreaPane.getChildren().removeFirst();

        playAreaPane.getChildren().addFirst(guiState.getPlayArea(username).getRoot());

        playerInfo.setVisible(true);
    }

    public void close(ActionEvent ignored) {
        playerInfo.setVisible(false);
    }

    public void home(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    /**
     * This method sets up and shows the error popup whenever an error occurs
     * (and consequently, an error event is fired).
     *
     * @param errorEvent the fired error event
     */
    public void showError(ErrorEvent errorEvent) {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(errorEvent.getErrorType());
        error.configurePopup(errorController);
        errorController.setErrorText(errorEvent.getErrorMsg());
        error.show(root);
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> {
            showError(errorEvent);
            errorEvent.consume();
        });

        root.addEventFilter(GUIEventTypes.SET_END_GAME_INFO_EVENT, e -> {
            setWinners(e.getWinnerUsernames());
            setPersonalObjectives(e.getPersonalObjectives());
        });

        root.addEventFilter(GUIEventTypes.SET_OBJECTIVE_POINTS_EVENT, e -> setPoints(e.getUsername(), e.getObjectivePoints()));
    }
}
