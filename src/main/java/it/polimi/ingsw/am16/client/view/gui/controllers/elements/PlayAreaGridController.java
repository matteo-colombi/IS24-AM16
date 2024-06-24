package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.*;

public class PlayAreaGridController {

    private static final RowConstraints rowConstraints = new RowConstraints(60, 60, 60, Priority.NEVER, VPos.CENTER, false);
    private static final ColumnConstraints columnConstraints = new ColumnConstraints(116, 116, 116, Priority.NEVER, HPos.CENTER, false);

    private int centerX, centerY;

    private int currWidth, currHeight;

    private Set<Position> placeablePositions;

    private Map<Position, GridFillerController> gridFillers;

    private String ownerUsername;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane playAreaGrid;

    /**
     * Initializes this element with default values and prepares it for later use.
     */
    @FXML
    public void initialize() {
        centerX = 0;
        centerY = 0;
        currWidth = 1;
        currHeight = 1;

        gridFillers = new HashMap<>();

        this.placeablePositions = new HashSet<>();
    }

    /**
     * Sets the center card of this play area.
     * This method should only be used on a freshly created element, when the grid is still empty, and ONLY ONCE.
     * @param cardController The controller for the center card.
     * @param legalPositions The set of positions where a card can be placed.
     * @param illegalPositions The set of positions where a card cannot be placed.
     */
    public void setCenterCard(CardController cardController, Set<Position> legalPositions, Set<Position> illegalPositions) {

        placeablePositions.addAll(legalPositions);
        placeablePositions.removeAll(illegalPositions);

        expandUp();
        expandDown();
        expandLeft();
        expandRight();

        putFillersInGrid(placeablePositions);

        GridPane.setConstraints(cardController.getRoot(), centerX, centerY);
        playAreaGrid.getChildren().addLast(cardController.getRoot());
    }

    /**
     * Puts the given card in the given position.
     * @param cardController The controller for the new card.
     * @param position The position where the new card should be placed.
     * @param addedLegalPositions The set of new positions where a card can be placed.
     * @param removedLegalPositions The set of positions where a card can no longer be placed.
     */
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

        putFillersInGrid(addedLegalPositions);

        GridPane.setConstraints(cardController.getRoot(), realCol, realRow);
        playAreaGrid.getChildren().addLast(cardController.getRoot());
    }

    /**
     * Expands the grid upwards by one tile.
     */
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

    /**
     * Expands the grid downwards by one tile.
     */
    private void expandDown() {
        playAreaGrid.getRowConstraints().addLast(rowConstraints);
        currHeight++;
    }

    /**
     * Expands the grid to the left by one tile.
     */
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

    /**
     * Expands the grid to the right by one tile.
     */
    private void expandRight() {
        playAreaGrid.getColumnConstraints().addLast(columnConstraints);
        currWidth++;
    }

    /**
     * @return The root node of this element.
     */
    public Parent getRoot() {
        return scrollPane;
    }

    /**
     * Creates a new grid filler, without adding it to the grid. Grid fillers are used as placeholders to detect when a card is dropped on them, and they are placed only where new cards can be placed on the grid.
     * @param position The position where the new grid filler will be placed.
     * @return The newly created grid filler's root pane.
     */
    private Pane addNewFiller(Position position) {
        if (gridFillers.get(position) == null) {
            GridFillerController gridFillerController = ElementFactory.getGridFiller();
            gridFillerController.setPosition(position);
            gridFillers.put(position, gridFillerController);
            return gridFillerController.getFillerPane();
        }
        return null;
    }

    /**
     * Removes the grid filler at the specified position.
     * @param fillerPosition The position of the filler to remove.
     */
    private void removeFiller(Position fillerPosition) {
        GridFillerController gridFiller = gridFillers.get(fillerPosition);

        if (gridFiller != null)
            playAreaGrid.getChildren().remove(gridFiller.getFillerPane());

        gridFillers.remove(fillerPosition);
    }

    /**
     * Creates and adds multiple new grid fillers to the grid.
     * @param positions The positions where new grid fillers should be placed.
     * @see PlayAreaGridController#putFillerInGrid
     */
    public void putFillersInGrid(Set<Position> positions) {
        for(Position position : positions) {
            putFillerInGrid(position);
        }
    }

    /**
     * Creates a new grid filler and puts it in the grid.
     * @param position The position where the new filler should be placed.
     */
    private void putFillerInGrid(Position position) {
        Pane fillerPane = addNewFiller(position);
        if (fillerPane == null)
            return;

        GridPane.setConstraints(fillerPane, position.x()+centerX, -position.y()+centerY);
        playAreaGrid.getChildren().addFirst(fillerPane);
    }

    /**
     * Sets the username of the owner of this play area grid.
     * @param username The username of the owner of this play area grid.
     */
    public void setOwnerUsername(String username) {
        this.ownerUsername = username;
    }

    /**
     * @return The username of the owner of this play area grid.
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }
}
