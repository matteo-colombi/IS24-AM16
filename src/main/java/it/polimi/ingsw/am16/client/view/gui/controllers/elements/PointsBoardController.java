package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.*;

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

    @FXML
    public void initialize() {
        pointSlots = new HashMap<>();
        pointSlots.put(0, points0);
        pointSlots.put(1, points1);
        pointSlots.put(2, points2);
        pointSlots.put(3, points3);
        pointSlots.put(4, points4);
        pointSlots.put(5, points5);
        pointSlots.put(6, points6);
        pointSlots.put(7, points7);
        pointSlots.put(8, points8);
        pointSlots.put(9, points9);
        pointSlots.put(10, points10);
        pointSlots.put(11, points11);
        pointSlots.put(12, points12);
        pointSlots.put(13, points13);
        pointSlots.put(14, points14);
        pointSlots.put(15, points15);
        pointSlots.put(16, points16);
        pointSlots.put(17, points17);
        pointSlots.put(18, points18);
        pointSlots.put(19, points19);
        pointSlots.put(20, points20);
        pointSlots.put(21, points21);
        pointSlots.put(22, points22);
        pointSlots.put(23, points23);
        pointSlots.put(24, points24);
        pointSlots.put(25, points25);
        pointSlots.put(26, points26);
        pointSlots.put(27, points27);
        pointSlots.put(28, points28);
        pointSlots.put(29, points29);

        pegsInSlots = new HashMap<>();
    }

    public void addPegInSlot(int slot, PlayerColor color) {
        List<PlayerColor> pegs = pegsInSlots.computeIfAbsent(slot, k -> new ArrayList<>());
        pegs.add(color);

        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor(pegs);
        putPegInSlot(slot, pegController);
    }

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

    private void putPegInSlot(int slot, PegController peg) {
        StackPane pegSlot = pointSlots.get(slot);
        Platform.runLater(() -> {
            pegSlot.getChildren().clear();
            pegSlot.getChildren().add(peg.getRoot());
        });
    }

    public Parent getRoot() {
        return root;
    }
}
