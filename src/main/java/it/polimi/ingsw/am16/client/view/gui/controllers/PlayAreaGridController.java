package it.polimi.ingsw.am16.client.view.gui.controllers;

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

public class PlayAreaGridController implements Initializable {

    private static final RowConstraints rowConstraints = new RowConstraints(60, 60, 60, Priority.NEVER, VPos.CENTER, false);
    private static final ColumnConstraints columnConstraints = new ColumnConstraints(116, 116, 116, Priority.NEVER, HPos.CENTER, false);

    private int centerX, centerY;

    private int currWidth, currHeight;

    private GUIState guiState;

    private Set<Position> placeablePositions;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane playAreaGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        centerX = 0;
        centerY = 0;
        currWidth = 1;
        currHeight = 1;

        guiState = CodexGUI.getGUI().getGuiState();

        guiState.setGridFillers(new HashMap<>());

        this.placeablePositions = new HashSet<>();

        //TODO remove. Just for testing
        playAreaGrid.setGridLinesVisible(true);

    }

    public void setCenterCard(CardController cardController, Set<Position> legalPositions, Set<Position> illegalPositions) {
        //WARNING: should only be called when the grid is still empty. Put that in the doc.

        placeablePositions.addAll(legalPositions);
        placeablePositions.removeAll(illegalPositions);

        Platform.runLater(() -> {
            expandUp();
            expandDown();
            expandLeft();
            expandRight();

            playAreaGrid.add(cardController.getRoot(), centerX, centerY);
        });
    }

    public void putCard(CardController cardController, Position position, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions) {

        final Position finalPosition = new Position(position.x(), -position.y());
        Platform.runLater(() -> {
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

            placeablePositions.removeAll(removedLegalPositions);

            for(Position addedLegal : addedLegalPositions) {
                Pane fillerPane = addNewFiller(addedLegal);
                if (fillerPane == null)
                    continue;

                playAreaGrid.add(fillerPane, addedLegal.x()+centerX, -addedLegal.y()+centerY);
            }
            placeablePositions.addAll(addedLegalPositions);

            playAreaGrid.add(cardController.getRoot(), realCol, realRow);
        });
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

        for(int i = 0; i < currWidth; i++) {
            int x = i-centerX;
            int y = -centerY;
            Position pos = new Position(x, -y);
            if (placeablePositions.contains(pos)) {
                Pane gridFiller = addNewFiller(pos);
                playAreaGrid.add(gridFiller, i, 0);
            }
        }
    }

    private void expandDown() {
        playAreaGrid.getRowConstraints().addLast(rowConstraints);
        currHeight++;

        for(int i = 0; i < currWidth; i++) {
            int x = i-centerX;
            int y = currHeight-1-centerY;
            Position pos = new Position(x, -y);
            if (placeablePositions.contains(pos)) {
                Pane gridFiller = addNewFiller(pos);
                playAreaGrid.add(gridFiller, i, currHeight-1);
            }
        }
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

        for(int i = 0; i < currHeight; i++) {
            int x = -centerX;
            int y = i-centerY;
            Position pos = new Position(x, -y);
            if (placeablePositions.contains(pos)) {
                Pane gridFiller = addNewFiller(pos);
                playAreaGrid.add(gridFiller, 0, i);
            }
        }
    }

    private void expandRight() {
        playAreaGrid.getColumnConstraints().addLast(columnConstraints);
        currWidth++;

        for(int i = 0; i < currHeight; i++) {
            int x = currWidth-1-centerX;
            int y = i-centerY;
            Position pos = new Position(x, -y);
            if (placeablePositions.contains(pos)) {
                Pane gridFiller = addNewFiller(pos);
                playAreaGrid.add(gridFiller, currWidth-1, i);
            }
        }
    }

    public Parent getRoot() {
        return scrollPane;
    }

    private Pane addNewFiller(Position position) {
        if (guiState.getGridFillerInPos(position) == null) {
            GridFillerController gridFillerController = ElementFactory.getGridFiller();
            gridFillerController.setPosition(position);
            guiState.putGridFillerInPos(position, gridFillerController);
            return gridFillerController.getFillerPane();
        }
        return null;
    }

    private void removeFiller(Position fillerPosition) {
        GridFillerController gridFiller = guiState.getGridFillerInPos(fillerPosition);

        if (gridFiller != null)
            playAreaGrid.getChildren().remove(gridFiller.getFillerPane());

        guiState.removeGridFillerInPos(fillerPosition);
    }
}
