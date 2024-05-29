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

    String paths[] = new String[]{"/assets/gui/rulebook/rulebook-1.png", "/assets/gui/rulebook/rulebook-2.png", "/assets/gui/rulebook/rulebook-3.png", "/assets/gui/rulebook/rulebook-4.png", "/assets/gui/rulebook/rulebook-5.png", "/assets/gui/rulebook/rulebook-6.png", "/assets/gui/rulebook/rulebook-7.png", "/assets/gui/rulebook/rulebook-8.png", "/assets/gui/rulebook/rulebook-9.png", "/assets/gui/rulebook/rulebook-10.png", "/assets/gui/rulebook/rulebook-11.png", "/assets/gui/rulebook/rulebook-12.png"};

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
