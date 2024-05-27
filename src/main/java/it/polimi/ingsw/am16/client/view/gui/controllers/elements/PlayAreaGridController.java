package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class PlayAreaGridController {

    private static final RowConstraints rowConstraints = new RowConstraints(60, 60, 60, Priority.NEVER, VPos.CENTER, false);
    private static final ColumnConstraints columnConstraints = new ColumnConstraints(116, 116, 116, Priority.NEVER, HPos.CENTER, false);

    private int centerX, centerY;

    private int currWidth, currHeight;

    private Set<Position> placeablePositions;

    private Map<Position, GridFillerController> gridFillers;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane playAreaGrid;

    @FXML
    public void initialize() {
        centerX = 0;
        centerY = 0;
        currWidth = 1;
        currHeight = 1;

        gridFillers = new HashMap<>();

        this.placeablePositions = new HashSet<>();

        //TODO remove. Just for testing
        playAreaGrid.setGridLinesVisible(true);

    }

    public void setCenterCard(CardController cardController, Set<Position> legalPositions, Set<Position> illegalPositions) {
        //WARNING: should only be called when the grid is still empty. Put that in the doc.

        placeablePositions.addAll(legalPositions);
        placeablePositions.removeAll(illegalPositions);

        expandUp();
        expandDown();
        expandLeft();
        expandRight();

        for(Position position : placeablePositions) {
            Pane fillerPane = addNewFiller(position);
            if (fillerPane == null)
                continue;

            playAreaGrid.add(fillerPane, position.x()+centerX, -position.y()+centerY);
        }

        playAreaGrid.add(cardController.getRoot(), centerX, centerY);
    }

    public void putCard(CardController cardController, Position position, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions) {
        placeablePositions.addAll(addedLegalPositions);
        placeablePositions.removeAll(removedLegalPositions);

        final Position finalPosition = new Position(position.x(), -position.y());
        int realCol = finalPosition.x() + centerX;
        int realRow = finalPosition.y() + centerY;
        while (realCol <= 0) {
            expandLeft();
            realCol++;
        }
        while (realRow <= 0) {
            expandUp();
            realRow++;
        }
        while(realCol >= currWidth-1) {
            expandRight();
        }
        while(realRow >= currHeight-1) {
            expandDown();
        }

        for (Position removedLegal : removedLegalPositions) {
            removeFiller(removedLegal);
        }

        for(Position addedLegal : addedLegalPositions) {
            Pane fillerPane = addNewFiller(addedLegal);
            if (fillerPane == null)
                continue;

            playAreaGrid.add(fillerPane, addedLegal.x()+centerX, -addedLegal.y()+centerY);
        }

        playAreaGrid.add(cardController.getRoot(), realCol, realRow);
    }

    private void expandUp() {
        playAreaGrid.getRowConstraints().addFirst(rowConstraints);
        currHeight++;
        centerY++;
        List<Node> children = new ArrayList<>(playAreaGrid.getChildren());
        for(Node node : children) {
            Integer col = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            if (col == null || row == null) continue;
            playAreaGrid.getChildren().remove(node);
            playAreaGrid.add(node, col, row+1);
        }
    }

    private void expandDown() {
        playAreaGrid.getRowConstraints().addLast(rowConstraints);
        currHeight++;
    }

    private void expandLeft() {
        playAreaGrid.getColumnConstraints().addFirst(columnConstraints);
        currWidth++;
        centerX++;
        List<Node> children = new ArrayList<>(playAreaGrid.getChildren());
        for(Node node : children) {
            Integer col = GridPane.getColumnIndex(node);
            Integer row = GridPane.getRowIndex(node);
            if (col == null || row == null) continue;
            playAreaGrid.getChildren().remove(node);
            playAreaGrid.add(node, col+1, row);
        }
    }

    private void expandRight() {
        playAreaGrid.getColumnConstraints().addLast(columnConstraints);
        currWidth++;
    }

    public Parent getRoot() {
        return scrollPane;
    }

    private Pane addNewFiller(Position position) {
        if (gridFillers.get(position) == null) {
            GridFillerController gridFillerController = ElementFactory.getGridFiller();
            gridFillerController.setPosition(position);
            gridFillers.put(position, gridFillerController);
            return gridFillerController.getFillerPane();
        }
        return null;
    }

    private void removeFiller(Position fillerPosition) {
        GridFillerController gridFiller = gridFillers.get(fillerPosition);

        if (gridFiller != null)
            playAreaGrid.getChildren().remove(gridFiller.getFillerPane());

        gridFillers.remove(fillerPosition);
    }
}
