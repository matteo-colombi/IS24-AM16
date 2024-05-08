package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;

import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.*;

public class CLIPlayArea {

    private final List<Position> cardPlacementOrder;
    private final Map<Position, BoardCard> field;
    private final Map<BoardCard, SideType> activeSides;
    private final CLIText playAreaText;
    private final CLIPointsTable pointsTable;
    private final CLIInfoTable infoTable;

    private final Set<Position> legalPositions;
    private final Set<Position> illegalPositions;

    private int viewCenter;

    private int minX;
    private int maxX;

    public CLIPlayArea(List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts, List<String> playerUsernames, Map<String, PlayerColor> playerColors, Map<String, Integer> gamePoints, Map<String, Integer> objectivePoints) {
        this.cardPlacementOrder = new ArrayList<>(cardPlacementOrder);
        this.field = new HashMap<>(field);
        this.activeSides = new HashMap<>(activeSides);

        this.playAreaText = new CLIText();

        this.legalPositions = legalPositions;
        this.illegalPositions = illegalPositions;

        this.pointsTable = new CLIPointsTable(playerUsernames, playerColors, gamePoints, objectivePoints);
        this.infoTable = new CLIInfoTable(resourceCounts, objectCounts);

        this.viewCenter = 0;

        this.minX = 0;
        this.maxX = 0;

        initializeText();
    }

    public CLIPlayArea() {
        this.cardPlacementOrder = new ArrayList<>();
        this.field = new HashMap<>();
        this.activeSides = new HashMap<>();

        this.playAreaText = new CLIText();

        this.legalPositions = new HashSet<>(Set.of(new Position(0, 0)));
        this.illegalPositions = new HashSet<>();

        this.pointsTable = new CLIPointsTable(List.of(), Map.of(), Map.of(), Map.of());
        this.infoTable = new CLIInfoTable(Map.of(), Map.of());

        this.viewCenter = 0;
        this.minX = 0;
        this.maxX = 0;
    }

    public void addCard(BoardCard card, SideType side, Position position, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.cardPlacementOrder.add(position);
        this.field.put(position, card);
        this.activeSides.put(card, side);

        mergeCard(card, position);

        this.legalPositions.addAll(addedLegalPositions);
        this.legalPositions.removeAll(removedLegalPositions);

        for (Position p : removedLegalPositions) {
            removePositionLabel(p);
        }
        for (Position p : addedLegalPositions) {
            addPositionLabel(p);
        }

        this.infoTable.update(resourceCounts, objectCounts);
    }

    public void updatePoints(Map<String, Integer> gamePoints, Map<String, Integer> objectivePoints) {
        this.pointsTable.update(gamePoints, objectivePoints);
    }

    private void initializeText() {
        for (Position pos : cardPlacementOrder) {
            BoardCard card = field.get(pos);
            if (card == null) continue;

            mergeCard(card, pos);
        }
        for (Position pos : legalPositions) {
            if (!illegalPositions.contains(pos))
                addPositionLabel(pos);
        }
    }

    private void mergeCard(BoardCard card, Position pos) {
        CLIText asset = CLIAssetRegistry.getCLIAssetRegistry().getCard(card.getName()).getSide(activeSides.get(card));
        int newPosCenterX = playAreaText.getOriginX() + (pos.x() * (CARD_WIDTH - OVERLAP_X));
        int newPosCenterY = playAreaText.getOriginY() + (-pos.y() * (CARD_HEIGHT - OVERLAP_Y));
        int startCol = newPosCenterX - CARD_WIDTH / 2;
        int startRow = newPosCenterY - CARD_HEIGHT / 2;
        playAreaText.mergeText(asset, startRow, startCol);

        minX = Math.min(minX, pos.x());
        maxX = Math.max(maxX, pos.x());

        if (pos.x() < viewCenter - VIEW_WIDTH / 2 + 1) {
            viewCenter = pos.x() - 1 + VIEW_WIDTH / 2;
        }
        if (pos.x() > viewCenter + VIEW_WIDTH / 2 - 1) {
            viewCenter = pos.x() + 1 - VIEW_WIDTH / 2;
        }
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
        int newPosCenterX = playAreaText.getOriginX() + (pos.x() * (CARD_WIDTH - OVERLAP_X));
        int newPosCenterY = playAreaText.getOriginY() + (-pos.y() * (CARD_HEIGHT - OVERLAP_Y));
        int startCol = newPosCenterX - LABEL_WIDTH / 2;
        int startRow = newPosCenterY - LABEL_HEIGHT / 2;
        playAreaText.mergeText(label.getLabel(), startRow, startCol);
    }

    private void placePointsTable(CLIText toPrintText, int startRow, int startCol) {
        startRow -= (INFO_TABLE_HEIGHT + pointsTable.getText().getHeight());
        startCol += PLAY_AREA_MARGIN_X;
        toPrintText.mergeText(pointsTable.getText(), startRow, startCol);
    }

    private void placeInfoTable(CLIText toPrintText, int startRow, int startCol) {
        startRow -= INFO_TABLE_HEIGHT;
        startCol += PLAY_AREA_MARGIN_X;
        toPrintText.mergeText(infoTable.getText(), startRow, startCol);
    }

    public void printPlayArea() {
        int startX = playAreaText.getOriginX() + (viewCenter - VIEW_WIDTH / 2) * (CARD_WIDTH - OVERLAP_X) - 5;
        int endX = playAreaText.getOriginX() + (viewCenter + VIEW_WIDTH / 2) * (CARD_WIDTH - OVERLAP_X) + 5;
        CLIText framedSubPlayArea = playAreaText.getSubText(startX, -20, endX, playAreaText.getHeight()).addFrame();
        int bottomRightCornerX = framedSubPlayArea.getWidth();
        int bottomRightCornerY = framedSubPlayArea.getHeight();

        if (bottomRightCornerY > 2 * CARD_HEIGHT)
            placePointsTable(framedSubPlayArea, bottomRightCornerY, bottomRightCornerX);

        placeInfoTable(framedSubPlayArea, bottomRightCornerY, bottomRightCornerX);
        framedSubPlayArea.printText();
    }

    public void moveView(int offset) {
        if (offset > 0 && offset + viewCenter + VIEW_WIDTH / 2 > maxX + 1) {
            viewCenter = maxX + 1 - VIEW_WIDTH / 2;
            return;
        }
        if (offset < 0 && offset + viewCenter - VIEW_WIDTH / 2 < minX - 1) {
            viewCenter = minX - 1 + VIEW_WIDTH / 2;
            return;
        }
        viewCenter += offset;
    }
}
