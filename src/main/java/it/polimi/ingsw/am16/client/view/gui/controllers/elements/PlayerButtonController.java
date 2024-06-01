package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerButtonController {

    @FXML
    private StackPane root;
    @FXML
    private Button button;
    @FXML
    private ImageView userIcon;

    @FXML
    public void initialize() {
        Image genericUser = new Image(Objects.requireNonNull(PlayerButtonController.class.getResourceAsStream(FilePaths.GUI_ICONS + "/users/generic.png")), userIcon.getFitWidth(), userIcon.getFitHeight(), true, true);
        userIcon.setImage(genericUser);
    }

    public void setCrown() {
        Image withCrown = new Image(Objects.requireNonNull(PlayerButtonController.class.getResourceAsStream(FilePaths.GUI_ICONS + "/users/generic-crown.png")), userIcon.getFitWidth(), userIcon.getFitHeight(), true, true);
        userIcon.setImage(withCrown);
    }

    public void setImage(int turnIndex) {
        Image userImage = new Image(Objects.requireNonNull(PlayerButtonController.class.getResourceAsStream(FilePaths.GUI_ICONS + "/users/" + turnIndex + ".png")), userIcon.getFitWidth(), userIcon.getFitHeight(), true, true);
        userIcon.setImage(userImage);
    }

    public void setColor(PlayerColor color) {
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
        button.setOnMouseEntered(e -> {
            if (currentlyShowingOtherHand.get() == null)
                otherHandController.getRoot().setVisible(true);
        });

        button.setOnMouseExited(e -> {
            if (currentlyShowingOtherHand.get() == null)
                otherHandController.getRoot().setVisible(false);
        });

        setOnMouseClicked(e -> {
            if (currentlyShowingOtherHand.get() == null) {
                otherHandController.getRoot().setVisible(true);
                currentlyShowingOtherHand.set(otherHandController);
            } else {
                otherHandController.getRoot().setVisible(false);
                currentlyShowingOtherHand.set(null);
            }
        });
    }

    public void setOnMouseClicked(EventHandler<MouseEvent> handler) {
        button.setOnMouseClicked(handler);
    }

    public void setDisabled(boolean disabled) {
        if (disabled) {
            button.setMouseTransparent(true);
            if (!button.getStyleClass().contains("disabled")) {
                button.getStyleClass().add("disabled");
            }
        } else {
            button.setMouseTransparent(false);
            button.getStyleClass().remove("disabled");
        }
    }

    public Parent getRoot() {
        return root;
    }
}
