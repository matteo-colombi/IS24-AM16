package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

public class GridFillerController {
    private static final PseudoClass LEGAL = PseudoClass.getPseudoClass("legal");
    private static final PseudoClass ILLEGAL = PseudoClass.getPseudoClass("illegal");

    @FXML
    private Pane fillerPane;

    private Position position;

    @FXML
    public void initialize() {
        fillerPane.setOnDragOver(dragEvent -> {
            dragEvent.acceptTransferModes(TransferMode.MOVE);
            dragEvent.consume();
        });

        fillerPane.setOnDragEntered(dragEvent -> {
            fillerPane.pseudoClassStateChanged(LEGAL, true);

            dragEvent.consume();
        });

        fillerPane.setOnDragExited(dragEvent -> {
            fillerPane.pseudoClassStateChanged(LEGAL, false);
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

            fillerPane.pseudoClassStateChanged(LEGAL, false);

            fillerPane.setOnDragOver(e -> {});
            fillerPane.setOnDragEntered(e -> {});
            fillerPane.setOnDragExited(e -> {});
            fillerPane.setOnDragDropped(e -> {});
        });
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Pane getFillerPane() {
        return fillerPane;
    }
}
