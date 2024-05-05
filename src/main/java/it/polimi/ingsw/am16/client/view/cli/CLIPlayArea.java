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
    private final CLIInfoTable infoTable;

    private final Set<Position> legalPositions;
    private final Set<Position> illegalPositions;

    private int viewCenter;

    private int minX;
    private int maxX;

    public CLIPlayArea(List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.cardPlacementOrder = new ArrayList<>(cardPlacementOrder);
        this.field = new HashMap<>(field);
        this.activeSides = new HashMap<>(activeSides);

        playAreaText = new CLIText();

        this.legalPositions = legalPositions;
        this.illegalPositions = illegalPositions;

        this.infoTable = new CLIInfoTable(resourceCounts, objectCounts);

        this.viewCenter = 0;

        this.minX = 0;
        this.maxX = 0;

        initializeText();
    }

    public CLIPlayArea() {
        cardPlacementOrder = new ArrayList<>();
        field = new HashMap<>();
        activeSides = new HashMap<>();

        playAreaText = new CLIText();
        legalPositions = new HashSet<>(Set.of(new Position(0, 0)));
        illegalPositions = new HashSet<>();

        infoTable = new CLIInfoTable(Map.of(), Map.of());

        this.viewCenter = 0;
    }

    public void addCard(BoardCard card, SideType side, Position position, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.cardPlacementOrder.add(position);
        this.field.put(position, card);
        this.activeSides.put(card, side);

        mergeCard(card, position);

        this.legalPositions.addAll(addedLegalPositions);
        this.legalPositions.removeAll(removedLegalPositions);

        for(Position p : removedLegalPositions) {
            removePositionLabel(p);
        }
        for(Position p : addedLegalPositions) {
            addPositionLabel(p);
        }

        this.infoTable.update(resourceCounts, objectCounts);

        minX = Math.min(minX, position.x());
        maxX = Math.max(maxX, position.x());
    }

    private void initializeText() {
        for(Position pos : cardPlacementOrder) {
            BoardCard card = field.get(pos);
            if(card == null) continue;

            mergeCard(card, pos);
        }
        for(Position pos : legalPositions) {
            if (!illegalPositions.contains(pos))
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

    private void placeInfoTable(CLIText toPrintText, int startX, int endX) {
        int startRow = playAreaText.getHeight() - INFO_TABLE_HEIGHT;
        startX = Math.max(startX, 0);
        int startCol = endX - startX + 4;
        toPrintText.mergeText(infoTable.getText(), startRow, startCol);
    }

    public void printPlayArea() {
        int startX = playAreaText.getOriginX() + (viewCenter - VIEW_WIDTH/2) * (CARD_WIDTH - OVERLAP_X)-5;
        int endX = playAreaText.getOriginX() + (viewCenter + VIEW_WIDTH/2) * (CARD_WIDTH - OVERLAP_X)+5;
        CLIText subPlayArea = playAreaText.getSubText(startX, 0, endX, playAreaText.getHeight());
        placeInfoTable(subPlayArea, startX, endX);
        subPlayArea.printText(true);
    }

    public void moveView(int offset) {
        if (offset > 0 && viewCenter + VIEW_WIDTH/2 > maxX) return;
        if (offset < 0 && viewCenter - VIEW_WIDTH/2 < minX) return;
        viewCenter += offset;
    }

    public void resetView() {
        viewCenter = 0;
    }
}
