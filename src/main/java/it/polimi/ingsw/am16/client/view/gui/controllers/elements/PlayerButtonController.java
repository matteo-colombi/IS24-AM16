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

    /**
     * Initializes the element with the default user icon.
     */
    @FXML
    public void initialize() {
        Image genericUser = new Image(Objects.requireNonNull(PlayerButtonController.class.getResourceAsStream(FilePaths.GUI_ICONS + "/users/generic.png")), userIcon.getFitWidth(), userIcon.getFitHeight(), true, true);
        userIcon.setImage(genericUser);
    }

    /**
     * Replaces the default user icon with an icon of the "owl with crown".
     */
    public void setCrown() {
        Image withCrown = new Image(Objects.requireNonNull(PlayerButtonController.class.getResourceAsStream(FilePaths.GUI_ICONS + "/users/generic-crown.png")), userIcon.getFitWidth(), userIcon.getFitHeight(), true, true);
        userIcon.setImage(withCrown);
    }

    /**
     * Sets the user icon with a predefined icon based on the player's turn index.
     * @param turnIndex The player's turn index, which determines its user icon.
     */
    public void setImage(int turnIndex) {
        Image userImage = new Image(Objects.requireNonNull(PlayerButtonController.class.getResourceAsStream(FilePaths.GUI_ICONS + "/users/" + turnIndex + ".png")), userIcon.getFitWidth(), userIcon.getFitHeight(), true, true);
        userIcon.setImage(userImage);
    }

    /**
     * Sets the player button color.
     * @param color The player's color choice.
     */
    public void setColor(PlayerColor color) {
        button.getStyleClass().add(color.name().toLowerCase());
    }

    /**
     * Sets whether this is grayed out and does not appear clickable.
     * @param active Whether this button should be grayed out and not clickable.
     */
    public void setActive(boolean active) {
        if (active) {
            if (!root.getStyleClass().contains("active")) {
                root.getStyleClass().add("active");
            }
        } else {
            root.getStyleClass().remove("active");
        }
    }

    /**
     * Sets the button's click event action.
     * @param otherPlayerInfoController The controller for the element to show when this button is clicked.
     * @param currentlyShowingOtherInfo A reference to the element that is already being shown and that should be hidden when this new element is displayed.
     */
    public void setActions(OtherPlayerInfoController otherPlayerInfoController, final AtomicReference<OtherPlayerInfoController> currentlyShowingOtherInfo) {
        setOnMouseClicked(e -> {
            if (currentlyShowingOtherInfo.get() == null) {
                otherPlayerInfoController.getRoot().setVisible(true);
                currentlyShowingOtherInfo.set(otherPlayerInfoController);
            } else {
                otherPlayerInfoController.getRoot().setVisible(false);
                currentlyShowingOtherInfo.set(null);
            }
        });
    }

    /**
     * Sets the button's click event action to be handled by the given {@link EventHandler}.
     * @param handler The event handler.
     */
    public void setOnMouseClicked(EventHandler<MouseEvent> handler) {
        button.setOnMouseClicked(handler);
    }

    /**
     * Sets whether this button is disabled.
     * @param disabled Whether this button is disabled.
     */
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

    /**
     * @return The root node of this element.
     */
    public Parent getRoot() {
        return root;
    }
}
