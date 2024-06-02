package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

    @FXML
    public void quit(ActionEvent ignored) {
        CodexGUI.getGUI().getStage().close();
    }

    @FXML
    public void goHome(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    public void setErrorText(String errorText) {
        this.errorText.setText(errorText);
    }

    public void setButtonText(String buttonText) {
        this.buttonText.setText(buttonText);
    }

    public void setButtonIcon(String iconPath) {
        buttonIcon.setImage(new Image(getClass().getResource(iconPath).toExternalForm()));
    }

    /**
     * This method sets the error popup to show up when the server has crashed,
     * thus forcing the application to close
     */
    public void quitStance(){
        this.errorText.setText("The server blew up!!!");
        this.buttonText.setText("Quit");
        buttonIcon.setImage(new Image(getClass().getResource("/assets/gui/icons/door.png").toExternalForm()));
        errorButton.setOnAction(this::quit);
    }

    /**
     * This method sets the error popup to show up when either the player or
     * another client has lost connection, and thus every player in the lobby is returned
     * to the home screen. Unlike {@link ErrorController#quitStance},
     * this method can set the error message to show who crashed between you
     * or another plauer.
     * @param errorMessage The message to show in the error popup
     */
    public void homeStance(String errorMessage){
        this.errorText.setText(errorMessage);
        this.buttonText.setText("Home");
        buttonIcon.setImage(new Image(getClass().getResource("/assets/gui/icons/home.png").toExternalForm()));
        errorButton.setOnAction(this::goHome);
    }

    //TODO refactor these to a single method with a switch?


}
