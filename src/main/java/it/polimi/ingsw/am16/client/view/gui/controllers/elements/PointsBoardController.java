package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for the points board.
 */
public class PointsBoardController {

    @FXML
    private AnchorPane root;
    @FXML
    private StackPane points0;
    @FXML
    private StackPane points1;
    @FXML
    private StackPane points2;
    @FXML
    private StackPane points3;
    @FXML
    private StackPane points4;
    @FXML
    private StackPane points5;
    @FXML
    private StackPane points6;
    @FXML
    private StackPane points7;
    @FXML
    private StackPane points8;
    @FXML
    private StackPane points9;
    @FXML
    private StackPane points10;
    @FXML
    private StackPane points11;
    @FXML
    private StackPane points12;
    @FXML
    private StackPane points13;
    @FXML
    private StackPane points14;
    @FXML
    private StackPane points15;
    @FXML
    private StackPane points16;
    @FXML
    private StackPane points17;
    @FXML
    private StackPane points18;
    @FXML
    private StackPane points19;
    @FXML
    private StackPane points20;
    @FXML
    private StackPane points21;
    @FXML
    private StackPane points22;
    @FXML
    private StackPane points23;
    @FXML
    private StackPane points24;
    @FXML
    private StackPane points25;
    @FXML
    private StackPane points26;
    @FXML
    private StackPane points27;
    @FXML
    private StackPane points28;
    @FXML
    private StackPane points29;

    private Map<Integer, StackPane> pointSlots;

    private Map<Integer, List<PlayerColor>> pegsInSlots;

    /**
     * Initializes this element, preparing it for later use.
     */
    @FXML
    public void initialize() {
        //I know it's not great, but it works and the alternative is hardcoding positions.

        pointSlots = Map.ofEntries(
                Map.entry(0, points0),
                Map.entry(1, points1),
                Map.entry(2, points2),
                Map.entry(3, points3),
                Map.entry(4, points4),
                Map.entry(5, points5),
                Map.entry(6, points6),
                Map.entry(7, points7),
                Map.entry(8, points8),
                Map.entry(9, points9),
                Map.entry(10, points10),
                Map.entry(11, points11),
                Map.entry(12, points12),
                Map.entry(13, points13),
                Map.entry(14, points14),
                Map.entry(15, points15),
                Map.entry(16, points16),
                Map.entry(17, points17),
                Map.entry(18, points18),
                Map.entry(19, points19),
                Map.entry(20, points20),
                Map.entry(21, points21),
                Map.entry(22, points22),
                Map.entry(23, points23),
                Map.entry(24, points24),
                Map.entry(25, points25),
                Map.entry(26, points26),
                Map.entry(27, points27),
                Map.entry(28, points28),
                Map.entry(29, points29)
        );

        pegsInSlots = new HashMap<>();
    }

    /**
     * Adds a peg in a point slot.
     * @param slot The slot in which the peg should be added.
     * @param color The color of the peg to add. If a peg is already in that slow, a multicolored peg will be created.
     */
    public void addPegInSlot(int slot, PlayerColor color) {
        List<PlayerColor> pegs = pegsInSlots.computeIfAbsent(slot, k -> new ArrayList<>());
        pegs.add(color);

        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor(pegs);
        putPegInSlot(slot, pegController);
    }

    /**
     * Removes a peg from a slot.
     * @param slot The slot from which the peg should be removed.
     * @param playerColor The color of the peg to remove. If other pegs are in the slot, only one color will be removed.
     */
    public void removePegInSlot(int slot, PlayerColor playerColor) {
        if (pegsInSlots.containsKey(slot)) {
            pegsInSlots.get(slot).remove(playerColor);
        }

        if (pegsInSlots.get(slot) == null || pegsInSlots.get(slot).isEmpty()) {
            Platform.runLater(() -> pointSlots.get(slot).getChildren().clear());
        } else {
            PegController pegController = ElementFactory.getPeg();
            pegController.setPegColor(pegsInSlots.get(slot));
            putPegInSlot(slot, pegController);
        }
    }

    /**
     * Actually puts a peg in a slot.
     * @param slot The slot.
     * @param peg The peg element.
     */
    private void putPegInSlot(int slot, PegController peg) {
        StackPane pegSlot = pointSlots.get(slot);
        Platform.runLater(() -> {
            pegSlot.getChildren().clear();
            pegSlot.getChildren().add(peg.getRoot());
        });
    }

    /**
     * @return The root node of this points board.
     */
    public Parent getRoot() {
        return root;
    }
}
