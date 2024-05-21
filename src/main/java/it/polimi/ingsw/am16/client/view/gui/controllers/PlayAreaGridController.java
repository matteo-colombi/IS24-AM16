package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.Card;
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

        guiState.setField(new HashMap<>());
        guiState.setGridFillers(new HashMap<>());
        guiState.setPlaceablePositions(new HashSet<>());

        //TODO remove. Just for testing
        guiState.setPlaceablePositions(Set.of(new Position(-2,0)));
        playAreaGrid.setGridLinesVisible(true);

    }

    public void setCenterCard(CardController cardController) {
        //WARNING: should only be called when the grid is still empty. Put that in the doc.

        guiState.putCardInField(new Position(0, 0), cardController.getCard());

        Platform.runLater(() -> {
            expandUp();
            expandDown();
            expandLeft();
            expandRight();

            playAreaGrid.add(cardController.getRoot(), centerX, centerY);
        });
    }

    public void putCard(CardController cardController, Position position) {
        guiState.putCardInField(position, cardController.getCard());

        removeFiller(position);

        Platform.runLater(() -> {
            int realCol = position.x() + centerX;
            int realRow = position.y() + centerY;
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
            playAreaGrid.add(cardController.getRoot(), realCol, realRow);
        });
    }

    public void expandUp() {
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
            Position pos = new Position(x, y);
            if (guiState.getPlaceablePositions().contains(pos)) {
                Pane gridFiller = addNewFiller(x, y);
                playAreaGrid.add(gridFiller, i, 0);
            }
        }
    }

    public void expandDown() {
        playAreaGrid.getRowConstraints().addLast(rowConstraints);
        currHeight++;

        for(int i = 0; i < currWidth; i++) {
            int x = i-centerX;
            int y = currHeight-1-centerY;
            Position pos = new Position(x, y);
            if (guiState .getPlaceablePositions().contains(pos)) {
                Pane gridFiller = addNewFiller(x, y);
                playAreaGrid.add(gridFiller, i, currHeight-1);
            }
        }
    }

    public void expandLeft() {
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
            Position pos = new Position(x, y);
            if (guiState.getPlaceablePositions().contains(pos)) {
                Pane gridFiller = addNewFiller(x, y);
                playAreaGrid.add(gridFiller, 0, i);
            }
        }
    }

    public void expandRight() {
        playAreaGrid.getColumnConstraints().addLast(columnConstraints);
        currWidth++;

        for(int i = 0; i < currHeight; i++) {
            int x = currWidth-1-centerX;
            int y = i-centerY;
            Position pos = new Position(x, y);
            if (guiState.getPlaceablePositions().contains(pos)) {
                Pane gridFiller = addNewFiller(x, y);
                playAreaGrid.add(gridFiller, currWidth-1, i);
            }
        }
    }

    public Parent getRoot() {
        return scrollPane;
    }

    public Pane addNewFiller(int x, int y) {
        GridFillerController gridFillerController = ElementFactory.getGridFiller();
        Position position = new Position(x, y);
        gridFillerController.setPosition(position);
        guiState.putGridFillerInPos(position, gridFillerController);
        return gridFillerController.getFillerPane();
    }

    public void removeFiller(Position fillerPosition) {
        GridFillerController gridFiller = guiState.getGridFillerInPos(fillerPosition);

        if (gridFiller != null)
            playAreaGrid.getChildren().remove(gridFiller.getFillerPane());

        guiState.removeGridFillerInPos(fillerPosition);
    }
}
