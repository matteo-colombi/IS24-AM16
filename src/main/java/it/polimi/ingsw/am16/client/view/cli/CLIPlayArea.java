package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;

import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.*;

/**
 * Utility class to manage a player's play area in the Command Line Interface.
 * This class handles placing cards and position labels on the play area, as well as initializing a play area with the player's starter card.
 * This class also handles the player's info table and places the points table beside the play area itself.
 */
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

    /**
     * Creates a new play area and initializes it with the given information.
     * @param cardPlacementOrder The order in which the cards in <code>field</code> were placed.
     * @param field The map containing all the cards in the position they were played in.
     * @param activeSides A map detailing what side is active for each card.
     * @param legalPositions The set of positions where a card can currently be placed.
     * @param illegalPositions The set of position where a card can currently not be played.
     * @param resourceCounts A map containing the amount of each resource currently visible on the player's play area.
     * @param objectCounts A map containing the amount of each object currently visible on the player's play area.
     * @param playerUsernames A list containing the usernames of the player in the game.
     * @param playerColors A map detailing each player's color.
     * @param gamePoints A map containing each player's amount of game points.
     * @param objectivePoints A map containing each player's amount of objective points.
     */
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

    /**
     * Creates a new empty play area. Used mainly for testing.
     */
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

    /**
     * Adds a card to this play area and updates its info table.
     * @param card The card that was played.
     * @param side The side on which the card was played.
     * @param position The position where the card was played.
     * @param addedLegalPositions The set of new positions where a card can be placed.
     * @param removedLegalPositions The set of positions where a card can no longer be placed.
     * @param resourceCounts The updated map containing the amounts of each resource currently visible on the player's play area.
     * @param objectCounts The updated map containing the amounts of each object currently visible on the player's play area.
     */
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

    /**
     * Updates the points on the points table.
     * @param gamePoints The updated map containing each player's amount of game points.
     * @param objectivePoints The updated map containing each player's amount of objective points.
     */
    public void updatePoints(Map<String, Integer> gamePoints, Map<String, Integer> objectivePoints) {
        this.pointsTable.update(gamePoints, objectivePoints);
    }

    /**
     * Initializes this play area's {@link CLIText} with all the cards and puts all the position labels where a card can currently be placed. Should only be used when creating a new play area.
     */
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

    /**
     * Adds the given card to this play area's {@link CLIText} representation.
     * @param card The new card to add.
     * @param pos The position where the card should be added.
     */
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

    /**
     * Adds a position label at the specified coordinates.
     * @param pos The position where the new label should be placed.
     */
    private void addPositionLabel(Position pos) {
        CLIPositionLabelAsset label = new CLIPositionLabelAsset(pos.x(), pos.y());
        putPositionLabel(label, pos);
    }

    /**
     * Removes a label, if present, from the specified coordinates.
     * @param pos The position where the label should be removed, if present.
     */
    private void removePositionLabel(Position pos) {
        CLIPositionLabelAsset label = CLIPositionLabelAsset.getEmptyLabel();
        putPositionLabel(label, pos);
    }

    /**
     * Actually merges a new position label in this play area's {@link CLIText}.
     * @param label The label to be merged.
     * @param pos The position where the new label should be merged.
     */
    private void putPositionLabel(CLIPositionLabelAsset label, Position pos) {
        int newPosCenterX = playAreaText.getOriginX() + (pos.x() * (CARD_WIDTH - OVERLAP_X));
        int newPosCenterY = playAreaText.getOriginY() + (-pos.y() * (CARD_HEIGHT - OVERLAP_Y));
        int startCol = newPosCenterX - LABEL_WIDTH / 2;
        int startRow = newPosCenterY - LABEL_HEIGHT / 2;
        playAreaText.mergeText(label.getLabel(), startRow, startCol);
    }

    /**
     * Merges this play area's points table with the provided {@link CLIText} that is about to be printed.
     * @param toPrintText The {@link CLIText} that is about to be printed. Note: this should not be this play area's original {@link CLIText}; instead, a copy should always be used for this method.
     * @param startRow The row where the top-left corner of the points table should go.
     * @param startCol The column where the top-left corner of the points table should go.
     */
    private void placePointsTable(CLIText toPrintText, int startRow, int startCol) {
        startRow -= (INFO_TABLE_HEIGHT + pointsTable.getText().getHeight());
        startCol += PLAY_AREA_MARGIN_X;
        toPrintText.mergeText(pointsTable.getText(), startRow, startCol);
    }

    /**
     * Merges this play area's info table with the provided {@link CLIText} that is about to be printed.
     * @param toPrintText The {@link CLIText} that is about to be printed. Note: this should not be this play area's original {@link CLIText}; instead, a copy should always be used for this method.
     * @param startRow The row where the top-left corner of the info table should go.
     * @param startCol The column where the top-left corner of the info table should go.
     */
    private void placeInfoTable(CLIText toPrintText, int startRow, int startCol) {
        startRow -= INFO_TABLE_HEIGHT;
        startCol += PLAY_AREA_MARGIN_X;
        toPrintText.mergeText(infoTable.getText(), startRow, startCol);
    }

    /**
     * Prints this play area to the terminal, including its info table and the points table. If the play area is too big, only a portion will be visible. The portion that is visible can be changed with the {@link CLIPlayArea#moveView} method.
     */
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

    /**
     * Changes the portion of this play area that is currently visible.
     * @param offset The amount of "card-length"s to move the view of. Positive numbers indicate movement to the right; negative numbers indicate movement to the left.
     */
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
