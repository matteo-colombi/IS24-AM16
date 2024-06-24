package it.polimi.ingsw.am16.client.view.gui.util;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Utility class to create informative popups.
 */
public class Popup {

    private Pane root;

    private final StackPane background;

    private boolean autoHide;

    /**
     * Creates a new popup with default settings.
     * By default, popups have no content, can only be closed explicitly via the {@link Popup#hide()} method and have a semitransparent black background to draw attention to the popup.
     */
    public Popup() {
        background = new StackPane();
        background.setBackground(Background.fill(Color.rgb(0, 0, 0, 0.4)));
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

    /**
     * Sets the parent {@link Pane} in which this popup should appear.
     * @param root The {@link Pane} in which this popup should appear.
     */
    public void setParent(Pane root) {
        this.root = root;
    }

    /**
     * Sets the content of this popup.
     * @param content The content of this popup.
     */
    public void setContent(Node content) {
        background.getChildren().clear();
        background.getChildren().add(content);
    }

    /**
     * Schedules the popup for display via the {@link Platform#runLater} method.
     */
    public void show() {
        Platform.runLater(() -> root.getChildren().addLast(background));
    }

    /**
     * Schedules the popup to be hidden via the {@link Platform#runLater} method.
     */
    public void hide() {
        Platform.runLater(() -> root.getChildren().remove(background));
    }

    /**
     * Sets this popup's background color.
     * @param backgroundColor The background color, a semi-transparent black by default.
     */
    public void setBackgroundColor(Paint backgroundColor) {
        background.setBackground(Background.fill(backgroundColor));
    }

    /**
     * Removes this popup's background color.
     */
    public void removeBackgroundColor() {
        background.setBackground(Background.EMPTY);
    }

    /**
     * Sets whether this popup should hide automatically when the user clicks outside the popup.
     * @param autoHide The auto hide property value, <code>false</code> by default.
     */
    public void setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
    }
}
