package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * The controller for GUI peg elements. Pegs are used to mark players' points on the board.
 */
public class PegController {

    @FXML
    private StackPane pegPane;

    @FXML
    private Circle peg;

    /**
     * Sets this peg's color.
     * @param color The peg's color. If <code>null</code> then the peg will be set to the color black.
     */
    public void setPegColor(PlayerColor color) {
        String styleClass;
        if (color == null) {
            styleClass = "black";
        } else {
            styleClass = color.name().toLowerCase();
        }
        peg.getStyleClass().clear();
        peg.getStyleClass().add(styleClass);
    }

    /**
     * Creates a peg with multiple colors. The peg will be sliced in equal parts for each color.
     * Useful when multiple players have the same amount of points.
     * @param colors The colors of this peg.
     */
    public void setPegColor(List<PlayerColor> colors) {
        if (colors.size() == 1) {
            setPegColor(colors.getFirst());
            return;
        }
        AnchorPane anchorPane = new AnchorPane();
        double radius = 13.3;
        double anchorSize = radius*2;
        anchorPane.setMinSize(anchorSize, anchorSize);
        anchorPane.setMaxSize(anchorSize, anchorSize);
        anchorPane.setPrefSize(anchorSize, anchorSize);
        Platform.runLater(() -> {
            pegPane.getChildren().clear();
            pegPane.getChildren().add(anchorPane);
        });
        int arcLength = 360/colors.size();
        for (int i = 0; i<colors.size(); i++) {
            Arc arc = new Arc();
            arc.setLength(arcLength);
            arc.setStartAngle(arcLength*i+90);
            arc.setRadiusX(radius);
            arc.setRadiusY(radius);
            arc.setType(ArcType.ROUND);
            String styleClass;
            if (colors.get(i) == null) {
                styleClass = "black";
            } else {
                styleClass = colors.get(i).name().toLowerCase();
            }
            arc.getStyleClass().clear();
            arc.getStyleClass().add(styleClass);
            arc.setTranslateX(radius);
            arc.setTranslateY(radius);
            Platform.runLater(() -> anchorPane.getChildren().add(arc));
        }
    }

    /**
     * Sets the radius of this circular peg. The default radius is 13.3.
     * @param radius The radius of this circular peg.
     */
    public void setPegRadius(double radius) {
        peg.setRadius(radius);
    }

    /**
     * Sets whether this peg is interactable. An interactable peg has a mouse hover effect.
     * @param interactable Whether this peg is interactable.
     */
    public void setInteractable(boolean interactable) {
        if (interactable) {
            if (!peg.getStyleClass().contains("interactable")) {
                peg.getStyleClass().add("interactable");
            }
        } else {
            peg.getStyleClass().remove("interactable");
        }
    }

    /**
     * Sets whether this peg is selected or not. A selected peg has a shadow effect.
     * @param selected Whether this peg is selected.
     */
    public void setSelected(boolean selected) {
        if (selected) {
            peg.getStyleClass().add("selected");
        } else {
            peg.getStyleClass().remove("selected");
        }
    }

    /**
     * Sets whether this peg is a placeholder. Placeholder pegs are invisible.
     * @param placeholder Whether this peg is a placeholder.
     */
    public void setPlaceholder(boolean placeholder) {
        if (placeholder) {
            peg.getStyleClass().add("placeholder");
        } else {
            peg.getStyleClass().remove("placeholder");
        }
    }

    /**
     * @return The root node of this peg element.
     */
    public Parent getRoot() {
        return pegPane;
    }
}
