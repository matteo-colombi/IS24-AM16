package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class PegController {

    @FXML
    private StackPane pegPane;

    @FXML
    private Circle peg;

    /**
     * Sets this peg's color.
     * @param color The peg's color. If <code>null</code> then the peg will be set to the color black.
     */
    public void setPegColor(PlayerColor color) {
        String styleClass;
        if (color == null) {
            styleClass = "black";
        } else {
            styleClass = color.name().toLowerCase();
        }
        peg.getStyleClass().clear();
        peg.getStyleClass().add(styleClass);
    }

    public Parent getRoot() {
        return pegPane;
    }
}
