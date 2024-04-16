package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIPlayArea {

    private static final int CARD_WIDTH = 21;
    private static final int CARD_HEIGHT = 7;
    private static final int OVERLAP_X = 5;
    private static final int OVERLAP_Y = 3;

    private final List<Position> cardPlacementOrder;
    private final Map<Position, BoardCard> field;
    private final Map<BoardCard, SideType> activeSides;
    private final CLIText playAreaText;

    public CLIPlayArea(List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        this.cardPlacementOrder = new ArrayList<>(cardPlacementOrder);
        this.field = new HashMap<>(field);
        this.activeSides = new HashMap<>(activeSides);
        this.playAreaText = new CLIText();
        initializeText();
    }

    public CLIPlayArea() {
        this.cardPlacementOrder = new ArrayList<>();
        this.field = new HashMap<>();
        this.activeSides = new HashMap<>();
        this.playAreaText = new CLIText();
    }

    public void addCard(BoardCard card, SideType side, Position position) {
        this.cardPlacementOrder.add(position);
        this.field.put(position, card);
        this.activeSides.put(card, side);
        mergeCard(card, position);
    }

    private void initializeText() {
        for(Position pos : cardPlacementOrder) {
            BoardCard card = field.get(pos);
            if(card == null) continue;

            mergeCard(card, pos);
        }
    }

    private void mergeCard(BoardCard card, Position pos) {
        CLIText asset = CLIAssetRegistry.getCLIAssetRegistry().getCard(card.getName()).getSide(activeSides.get(card));
        int newPosCenterX = playAreaText.getOriginX() + (pos.x() * (CARD_WIDTH - OVERLAP_X));
        int newPosCenterY = playAreaText.getOriginY() + (-pos.y() * (CARD_HEIGHT - OVERLAP_Y));
        int startCol = newPosCenterX - CARD_WIDTH/2;
        int startRow = newPosCenterY - CARD_HEIGHT/2;
        playAreaText.mergeText(asset, startRow, startCol);
    }

    public void printPlayArea() {
        playAreaText.printText();
    }
}
