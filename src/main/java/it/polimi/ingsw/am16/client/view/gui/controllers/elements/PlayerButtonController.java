package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class PlayerButtonController {

    @FXML
    private StackPane root;
    @FXML
    private Button button;

    public void setColor(PlayerColor color) {
        button.getStyleClass().clear();
        button.getStyleClass().add("button");
        button.getStyleClass().add(color.name().toLowerCase());
    }

    public void setActive(boolean active) {
        if (active) {
            if (!button.getStyleClass().contains("active")) {
                button.getStyleClass().add("active");
            }
        } else {
            button.getStyleClass().remove("active");
        }
    }

    public Parent getRoot() {
        return root;
    }
}
