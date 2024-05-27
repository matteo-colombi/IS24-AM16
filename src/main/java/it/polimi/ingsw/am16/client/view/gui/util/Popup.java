package it.polimi.ingsw.am16.client.view.gui.util;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Popup {

    private Pane root;

    private final StackPane background;

    private boolean autoHide;

    public Popup() {
        background = new StackPane();
        background.setBackground(Background.fill(Color.rgb(0, 0, 0, 0.5)));
        background.getStyleClass().add("popup-background");
        background.setMouseTransparent(false);
        background.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Node intersectedNode = mouseEvent.getPickResult().getIntersectedNode();
            if (autoHide && intersectedNode == background) {
                hide();
            }
        });
        autoHide = false;
    }

    public void setParent(Pane root) {
        this.root = root;
    }

    public void setContent(Node content) {
        background.getChildren().clear();
        background.getChildren().add(content);
    }

    public void show() {
        root.getChildren().addLast(background);
    }

    public void hide() {
        root.getChildren().remove(background);
    }

    public void setBackgroundColor(Paint backgroundColor) {
        background.setBackground(Background.fill(backgroundColor));
    }

    public void removeBackgroundColor() {
        background.setBackground(Background.EMPTY);
    }

    public void setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
    }
}
