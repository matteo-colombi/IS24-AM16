package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;

public class CLIPlayArea {

    private static final int CARD_WIDTH = 21;
    private static final int CARD_HEIGHT = 7;
    private static final int OVERLAP_X = 5;
    private static final int OVERLAP_Y = 3;

    private static final int LABEL_WIDTH = 11;
    private static final int LABEL_HEIGHT = 3;

    private final List<Position> cardPlacementOrder;
    private final Map<Position, BoardCard> field;
    private final Map<BoardCard, SideType> activeSides;
    private final CLIText playAreaText;

    private final Set<Position> placeablePositions;

    public CLIPlayArea(List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        this.cardPlacementOrder = new ArrayList<>(cardPlacementOrder);
        this.field = new HashMap<>(field);
        this.activeSides = new HashMap<>(activeSides);
        playAreaText = new CLIText();
        placeablePositions = new HashSet<>();
        for(Position p : cardPlacementOrder) {
            BoardCard card = field.get(p);
            CardSide side = card.getCardSideBySideType(activeSides.get(card));
            Map<CornersIdx, CornerType> corners = side.getCorners();
            for(CornersIdx idx : corners.keySet()) {
                if (corners.get(idx) == CornerType.BLOCKED) {
                    placeablePositions.remove(p.addOffset(idx.getOffset()));
                    continue;
                }
                if (field.get(p.addOffset(idx.getOffset())) == null) {
                    placeablePositions.add(p.addOffset(idx.getOffset()));
                }
            }
        }
        initializeText();
    }

    public CLIPlayArea() {
        cardPlacementOrder = new ArrayList<>();
        field = new HashMap<>();
        activeSides = new HashMap<>();
        playAreaText = new CLIText();
        placeablePositions = new HashSet<>(Set.of(new Position(0, 0)));
    }

    public void addCard(BoardCard card, SideType side, Position position) {
        this.cardPlacementOrder.add(position);
        this.field.put(position, card);
        this.activeSides.put(card, side);
        mergeCard(card, position);
        placeablePositions.remove(position);
        Map<CornersIdx, CornerType> corners = card.getCardSideBySideType(side).getCorners();
        Set<Position> newPlaceablePositions = new HashSet<>();
        Set<Position> toRemovePlaceablePositions = new HashSet<>();
        for(CornersIdx idx : corners.keySet()) {
            if (corners.get(idx) == CornerType.BLOCKED) {
                toRemovePlaceablePositions.add(position.addOffset(idx.getOffset()));
                continue;
            }
            if (field.get(position.addOffset(idx.getOffset())) == null) {
                newPlaceablePositions.add(position.addOffset(idx.getOffset()));
            }
        }
        for(Position p : toRemovePlaceablePositions) {
            if (placeablePositions.contains(p)) {
                removeLabel(p);
            }
        }
        newPlaceablePositions.removeAll(toRemovePlaceablePositions);
        for(Position p : newPlaceablePositions) {
            if (field.get(p) == null) {
                addLabel(p);
            }
        }
        placeablePositions.removeAll(toRemovePlaceablePositions);
        placeablePositions.addAll(newPlaceablePositions);
    }

    private void initializeText() {
        for(Position pos : cardPlacementOrder) {
            BoardCard card = field.get(pos);
            if(card == null) continue;

            mergeCard(card, pos);
        }
        for(Position pos : placeablePositions) {
            addLabel(pos);
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

    private void addLabel(Position pos) {
        CLILabelAsset label = new CLILabelAsset(pos.x(), pos.y());
        putLabel(label, pos);
    }

    private void removeLabel(Position pos) {
        CLILabelAsset label = CLILabelAsset.getEmptyLabel();
        putLabel(label, pos);
    }

    private void putLabel(CLILabelAsset label, Position pos) {
        int newPosCenterX = playAreaText.getOriginX() + (pos.x() * (CARD_WIDTH-OVERLAP_X));
        int newPosCenterY = playAreaText.getOriginY() + (-pos.y() * (CARD_HEIGHT-OVERLAP_Y));
        int startCol = newPosCenterX - LABEL_WIDTH/2;
        int startRow = newPosCenterY - LABEL_HEIGHT/2;
        playAreaText.mergeText(label.getLabel(), startRow, startCol);
    }

    public void printPlayArea() {
        playAreaText.printText();
    }

    public Set<Position> getPlaceablePositions() {
        return placeablePositions;
    }
}
