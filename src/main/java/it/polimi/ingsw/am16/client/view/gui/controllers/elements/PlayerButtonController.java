package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.concurrent.atomic.AtomicReference;

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
            if (!root.getStyleClass().contains("active")) {
                root.getStyleClass().add("active");
            }
        } else {
            root.getStyleClass().remove("active");
        }
    }

    public void setHovers(OtherHandController otherHandController, final AtomicReference<OtherHandController> currentlyShowingOtherHand) {
        button.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
            if (currentlyShowingOtherHand.get() == null)
                otherHandController.getRoot().setVisible(true);
        });

        button.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
            if (currentlyShowingOtherHand.get() == null)
                otherHandController.getRoot().setVisible(false);
        });

        button.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (currentlyShowingOtherHand.get() == null) {
                otherHandController.getRoot().setVisible(true);
                currentlyShowingOtherHand.set(otherHandController);
            } else {
                otherHandController.getRoot().setVisible(false);
                currentlyShowingOtherHand.set(null);
            }
        });
    }

    public Parent getRoot() {
        return root;
    }
}
