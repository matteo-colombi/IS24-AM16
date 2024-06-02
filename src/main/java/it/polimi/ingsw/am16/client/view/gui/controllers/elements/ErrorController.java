package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.ErrorEvent;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Controller for the error popup.
 * It's meant to appear when an error occurs and the player needs to be notified
 * and to perform an action before continuing to use the application.
 */
public class ErrorController {


    @FXML
    public ImageView buttonIcon;
    @FXML
    public Button errorButton;
    @FXML
    public StackPane errorBox;
    public Text errorText;
    public Text buttonText;

    public String[] paths = new String[]{"/assets/gui/icons/home.png", "/assets/gui/icons/door.png"};

    @FXML
    public void initialize() {

    }

    /**
     * Action method to quit the application.
     * It's made to be assigned to the button of the popup.
     * @param ignored
     */
    @FXML
    public void quit(ActionEvent ignored) {
        CodexGUI.getGUI().getStage().close();
    }

    /**
     * Action method to return to the home screen.
     * It's made to be assigned to the button of the popup.
     * @param ignored
     */
    @FXML
    public void goHome(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }


    /**
     * Shows the error message and sets the button to perform the correct action,
     * preparing the popup to be shown.
     * @param e The error event that triggered the popup.
     */
    public void show(ErrorEvent e){
        this.errorText.setText(e.getErrorMsg());
        switch(e.getErrorType()){
            case JOIN_ERROR:
                //maybe make it go to the games list instead?
                this.buttonText.setText("Home");
                buttonIcon.setImage(new Image(getClass().getResource(FilePaths.GUI_ICONS + "/home.png").toExternalForm()));
                errorButton.setOnAction(this::goHome);
                break;
            case CONNECTION_DEAD:
                this.buttonText.setText("Quit");
                buttonIcon.setImage(new Image(getClass().getResource(FilePaths.GUI_ICONS + "/door.png").toExternalForm()));
                errorButton.setOnAction(this::quit);
                break;
            case OTHER_PLAYER_DISCONNECTED:
                this.buttonText.setText("Home");
                buttonIcon.setImage(new Image(getClass().getResource(FilePaths.GUI_ICONS + "/home.png").toExternalForm()));
                errorButton.setOnAction(this::goHome);
                break;
            default:
                this.buttonText.setText("Quit");
                buttonIcon.setImage(new Image(getClass().getResource(FilePaths.GUI_ICONS + "/door.png").toExternalForm()));
                errorButton.setOnAction(this::goHome);
        }
    }


}
