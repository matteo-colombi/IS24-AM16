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

    public void setPegRadius(double radius) {
        peg.setRadius(radius);
    }

    public void setInteractable(boolean interactable) {
        if (interactable) {
            if (!peg.getStyleClass().contains("interactable")) {
                peg.getStyleClass().add("interactable");
            }
        } else {
            peg.getStyleClass().remove("interactable");
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            peg.getStyleClass().add("selected");
        } else {
            peg.getStyleClass().remove("selected");
        }
    }

    public void setPlaceholder(boolean placeholder) {
        if (placeholder) {
            peg.getStyleClass().add("placeholder");
        } else {
            peg.getStyleClass().remove("placeholder");
        }
    }

    public Parent getRoot() {
        return pegPane;
    }
}
