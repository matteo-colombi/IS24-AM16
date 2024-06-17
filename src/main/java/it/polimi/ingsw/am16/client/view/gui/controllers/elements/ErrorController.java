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


    private String[] paths = new String[]{"/assets/gui/icons/home.png", "/assets/gui/icons/door.png"};

    @FXML
    public void initialize() {

    }

    public ImageView getButtonIcon() {
        return buttonIcon;
    }

    public Button getErrorButton() {
        return errorButton;
    }

    public Text getButtonText() {
        return buttonText;
    }



    /**
     * Action method to quit the application.
     * It's made to be assigned to the button of the popup.
     */
    public void quit(ActionEvent ignored) {
        Platform.exit();
    }

    /**
     * Action method to return to the home screen.
     * It's made to be assigned to the button of the popup.
     */
    public void goHome(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    /**
     * Action method to close the error popup. Made to be assigned to the button of the popup.
     */
    public void close(ActionEvent ignored) {
        if (popup != null) {
            popup.hide();
        }
    }


    public void setErrorText(String errorText) {
        this.errorText.setText(errorText);
    }

    public Parent getRoot() {
        return root;
    }

    public void show(Pane parent) {
        popup = new Popup();
        popup.setAutoHide(false);
        popup.setContent(root);
        popup.setParent(parent);
        popup.show();
    }

}
