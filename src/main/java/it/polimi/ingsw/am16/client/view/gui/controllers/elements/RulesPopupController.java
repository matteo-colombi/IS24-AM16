package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.util.GUIAssetRegistry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Objects;

public class RulesPopupController {
    @FXML
    StackPane root;
    @FXML
    ImageView rulesBook;

    private int idx;

    private static final List<String> paths = GUIAssetRegistry.getGUIRulebookPaths();

    @FXML
    public void initialize() {
        idx = 0;
    }

    @FXML
    public void prev(ActionEvent ignored) {
        idx = (paths.size() + idx - 1) % paths.size();

        rulesBook.setImage(new Image(Objects.requireNonNull(RulesPopupController.class.getResourceAsStream(paths.get(idx)))));
    }

    @FXML
    public void next(ActionEvent ignored) {
        idx = (paths.size() + idx + 1) % paths.size();

        rulesBook.setImage(new Image(Objects.requireNonNull(RulesPopupController.class.getResourceAsStream(paths.get(idx)))));
    }

    @FXML
    public void back(ActionEvent ignored) {
        root.setVisible(false);
    }

    public Parent getRoot() {
        return root;
    }
}
