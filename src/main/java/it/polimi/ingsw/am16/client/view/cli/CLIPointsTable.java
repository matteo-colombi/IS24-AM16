package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to manage a table to display information about the points that each player has scored.
 */
public class CLIPointsTable {

    private final static int COLOR_COLUMN = 3;
    private final static int USERNAME_COLUMN = 6;
    private final static int POINTS_COLUMN = 19;

    private final List<String> playerUsernames;
    private final Map<String, PlayerColor> playerColors;

    private final CLIText pointsTableAsset;

    /**
     * Creates a new points table and initializes it with the given information.
     * @param playerUsernames A list containing the player's usernames.
     * @param playerColors A map containing each player's color.
     * @param gamePoints A map containing each player's amount of game points, which are points scored with resource and gold cards (not objectives).
     * @param objectivePoints A map containing each player's amount of objective points, which are points scored with objective cards.
     */
    public CLIPointsTable(List<String> playerUsernames, Map<String, PlayerColor> playerColors, Map<String, Integer> gamePoints, Map<String, Integer> objectivePoints) {
        this.playerUsernames = playerUsernames;
        this.playerColors = playerColors;

        List<String> pointsTableComponents = new ArrayList<>();

        //This is horrible but kind of necessary since the height of this component is dynamic.
        //Technically we could make a CLIText for the top and bottom margins, one for the center bits and then merge them, but it's not worth it.
        pointsTableComponents.add("╔══════════════════════╗");

        for (int i = 0; i < playerUsernames.size(); i++) {
            pointsTableComponents.add("║                      ║");
        }

        pointsTableComponents.add("╚══════════════════════╝");

        this.pointsTableAsset = new CLIText(pointsTableComponents.toArray(new String[0]));

        update(gamePoints, objectivePoints);
    }

    /**
     * Updates this points table with the new amounts of points.
     * @param gamePoints The new amounts of each player's game points.
     * @param objectivePoints The new amounts of each player's objective points.
     */
    public void update(Map<String, Integer> gamePoints, Map<String, Integer> objectivePoints) {
        Map<String, Integer> totalPoints = new HashMap<>();

        for (String username : this.playerUsernames) {
            totalPoints.put(username, gamePoints.getOrDefault(username, 0) + objectivePoints.getOrDefault(username, 0));
        }

        List<String> sortedUsernames = this.playerUsernames
                .stream()
                .sorted((s1, s2) -> {
                            if (totalPoints.get(s1).equals(totalPoints.get(s2))) {
                                return Integer.compare(objectivePoints.getOrDefault(s1, 0), objectivePoints.getOrDefault(s2, 0));
                            } else {
                                return Integer.compare(totalPoints.get(s1), totalPoints.get(s2));
                            }
                        }
                )
                .toList()
                .reversed();

        for (int i = 0; i < sortedUsernames.size(); i++) {
            String username = sortedUsernames.get(i);
            CLIText colorLabel = new CLIText("██", this.playerColors.get(username));
            CLIText usernameLabel = new CLIText(String.format("%-10s", username));
            CLIText pointsLabel = new CLIText(String.format("%02d", totalPoints.get(username)));

            this.pointsTableAsset.mergeText(colorLabel, i + 1, COLOR_COLUMN);
            this.pointsTableAsset.mergeText(usernameLabel, i + 1, USERNAME_COLUMN);
            this.pointsTableAsset.mergeText(pointsLabel, i + 1, POINTS_COLUMN);
        }
    }

    /**
     * @return A print-ready {@link CLIText} representation of this points table.
     */
    public CLIText getText() {
        return this.pointsTableAsset;
    }
}
