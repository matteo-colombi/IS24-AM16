package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.Popup;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Controller for the error popup.
 * It's meant to appear when an error occurs and the player needs to be notified
 * and to perform an action before continuing to use the application.
 */
public class ErrorController {
    @FXML
    private ImageView buttonIcon;
    @FXML
    private Button errorButton;
    @FXML
    private StackPane root;
    @FXML
    private Text errorText;
    @FXML
    private Text buttonText;

    private Popup popup;

    /**
     * @return The image view for the icon that has to be showed inside the button on the popup. Change the image as needed with the method {@link ImageView#setImage}.
     */
    public ImageView getButtonIcon() {
        return buttonIcon;
    }

    /**
     * @return The button element on the popup. Change the action when clicked as needed with {@link Button#setOnAction}.
     */
    public Button getErrorButton() {
        return errorButton;
    }

    /**
     * @return The text element inside the button of the popup. Change as needed with {@link Text#setText}.
     */
    public Text getButtonText() {
        return buttonText;
    }

    /**
     * Action method to quit the application.
     * Made to be assigned to the button of the popup.
     */
    public void quit(ActionEvent ignored) {
        Platform.exit();
    }

    /**
     * Action method to return to the home screen.
     * Made to be assigned to the button of the popup.
     */
    public void goHome(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    /**
     * Action method to close the error popup.
     * Made to be assigned to the button of the popup.
     */
    public void close(ActionEvent ignored) {
        if (popup != null) {
            popup.hide();
        }
    }

    /**
     * Sets the title of the error popup.
     * @param errorText The text to be displayed.
     */
    public void setErrorText(String errorText) {
        this.errorText.setText(errorText);
    }

    /**
     * @return The root element of this error popup element.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Displays the popup as a child of the given node. Uses {@link Popup}.
     * @param parent The parent node inside which the popup should be shown. Usually the root of the scene.
     */
    public void show(Pane parent) {
        popup = new Popup();
        popup.setAutoHide(false);
        popup.setContent(root);
        popup.setParent(parent);
        popup.show();
    }

}
