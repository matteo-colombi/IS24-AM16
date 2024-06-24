package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Controller for fillers that are supposed to be placed inside the {@link PlayAreaGridController} to detect the player dropping cards.
 */
public class GridFillerController {
    @FXML
    private Pane fillerPane;

    private Position position;

    /**
     * Initializes this grid filler and sets the needed events.
     */
    @FXML
    public void initialize() {
        fillerPane.setOnDragOver(dragEvent -> {
            dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        });

        fillerPane.setOnDragEntered(dragEvent -> {
            if (!fillerPane.getStyleClass().contains("legal")) {
                fillerPane.getStyleClass().add("legal");
            }

            dragEvent.consume();
        });

        fillerPane.setOnDragExited(dragEvent -> {
            fillerPane.getStyleClass().remove("legal");
            dragEvent.consume();
        });

        fillerPane.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();

            Card draggedCard = (Card) db.getContent(GUIState.draggedCard);

            ClipboardContent clipboard = new ClipboardContent();
            clipboard.put(GUIState.draggedCard, draggedCard);
            clipboard.put(GUIState.droppedOnPos, position);
            db.setContent(clipboard);

            dragEvent.setDropCompleted(true);
            dragEvent.consume();

            fillerPane.getStyleClass().remove("legal");

            fillerPane.setOnDragOver(e -> {});
            fillerPane.setOnDragEntered(e -> {});
            fillerPane.setOnDragExited(e -> {});
            fillerPane.setOnDragDropped(e -> {});
        });
    }

    /**
     * @return The position of this grid filler in the play area grid.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of this grid filler inside the play area grid.
     * @param position The position of this grid filler inside the play area grid.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * @return The root pane of this grid filler.
     */
    public Pane getFillerPane() {
        return fillerPane;
    }
}
