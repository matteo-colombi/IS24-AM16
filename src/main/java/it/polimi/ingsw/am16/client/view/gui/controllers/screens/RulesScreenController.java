package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;


public class RulesScreenController {
    @FXML
    StackPane root;
    @FXML
    Button prevButton;
    @FXML
    Button nextButton;
    @FXML
    ImageView rulesBook;

    private int i;

    String paths[] = new String[]{"/assets/gui/icons/check.png", "/assets/gui/icons/close.png", "/assets/gui/icons/add.png"};

    @FXML
    public void initialize() {
        i = 0;
    }

    @FXML
    public void prev(ActionEvent ignored) {
        i--;
        i = (paths.length + i) % paths.length;
        System.out.println((paths.length + i) % paths.length);
        rulesBook.setImage(new Image(getClass().getResource(paths[(paths.length + i) % paths.length]).toExternalForm()));
    }

    @FXML
    public void next(ActionEvent ignored) {
        i++;
        i = (paths.length + i) % paths.length;

        System.out.println((paths.length + i) % paths.length);
        rulesBook.setImage(new Image(getClass().getResource(paths[(paths.length + i) % paths.length]).toExternalForm()));
    }

    @FXML
    public void back(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }


}
