package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;

import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.*;

public class CLIPlayArea {

    private final List<Position> cardPlacementOrder;
    private final Map<Position, BoardCard> field;
    private final Map<BoardCard, SideType> activeSides;
    private final CLIText playAreaText;

    private final Set<Position> placeablePositions;

    private int viewCenter;

    private int minX;
    private int maxX;

    public CLIPlayArea(List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        this.cardPlacementOrder = new ArrayList<>(cardPlacementOrder);
        this.field = new HashMap<>(field);
        this.activeSides = new HashMap<>(activeSides);

        playAreaText = new CLIText();
        placeablePositions = new HashSet<>();

        this.viewCenter = 0;

        this.minX = 0;
        this.maxX = 0;

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
            minX = Math.min(minX, p.x());
            maxX = Math.max(maxX, p.x());
        }

        initializeText();
    }

    public CLIPlayArea() {
        cardPlacementOrder = new ArrayList<>();
        field = new HashMap<>();
        activeSides = new HashMap<>();

        playAreaText = new CLIText();
        placeablePositions = new HashSet<>(Set.of(new Position(0, 0)));

        this.viewCenter = 0;
    }

    public void addCard(BoardCard card, SideType side, Position position) {
        this.cardPlacementOrder.add(position);
        this.field.put(position, card);
        this.activeSides.put(card, side);

        mergeCard(card, position);

        placeablePositions.remove(position);

        Set<Position> newPlaceablePositions = new HashSet<>();
        Set<Position> toRemovePlaceablePositions = new HashSet<>();

        Map<CornersIdx, CornerType> corners = card.getCardSideBySideType(side).getCorners();

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
                removePositionLabel(p);
            }
        }
        newPlaceablePositions.removeAll(toRemovePlaceablePositions);
        for(Position p : newPlaceablePositions) {
            if (field.get(p) == null) {
                addPositionLabel(p);
            }
        }
        placeablePositions.removeAll(toRemovePlaceablePositions);
        placeablePositions.addAll(newPlaceablePositions);

        minX = Math.min(minX, position.x());
        maxX = Math.max(maxX, position.x());
    }

    private void initializeText() {
        for(Position pos : cardPlacementOrder) {
            BoardCard card = field.get(pos);
            if(card == null) continue;

            mergeCard(card, pos);
        }
        for(Position pos : placeablePositions) {
            addPositionLabel(pos);
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

    private void addPositionLabel(Position pos) {
        CLIPositionLabelAsset label = new CLIPositionLabelAsset(pos.x(), pos.y());
        putPositionLabel(label, pos);
    }

    private void removePositionLabel(Position pos) {
        CLIPositionLabelAsset label = CLIPositionLabelAsset.getEmptyLabel();
        putPositionLabel(label, pos);
    }

    private void putPositionLabel(CLIPositionLabelAsset label, Position pos) {
        int newPosCenterX = playAreaText.getOriginX() + (pos.x() * (CARD_WIDTH-OVERLAP_X));
        int newPosCenterY = playAreaText.getOriginY() + (-pos.y() * (CARD_HEIGHT-OVERLAP_Y));
        int startCol = newPosCenterX - LABEL_WIDTH/2;
        int startRow = newPosCenterY - LABEL_HEIGHT/2;
        playAreaText.mergeText(label.getLabel(), startRow, startCol);
    }

    public void printPlayArea() {
        int startX = playAreaText.getOriginX() + (viewCenter - VIEW_WIDTH/2) * (CARD_WIDTH - OVERLAP_X)-5;
        int endX = playAreaText.getOriginX() + (viewCenter + VIEW_WIDTH/2) * (CARD_WIDTH - OVERLAP_X)+5;
        playAreaText.printText(startX, 0, endX, playAreaText.getHeight(), true);
    }

    public Set<Position> getPlaceablePositions() {
        return placeablePositions;
    }

    public void moveView(int offset) {
        if (offset > 0 && viewCenter + VIEW_WIDTH/2 > maxX) return;
        if (offset < 0 && viewCenter - VIEW_WIDTH/2 < minX) return;
        viewCenter += offset;
    }
}
