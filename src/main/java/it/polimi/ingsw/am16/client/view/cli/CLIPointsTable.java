package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIPointsTable {

    private final static int COLOR_COLUMN = 3;
    private final static int USERNAME_COLUMN = 6;
    private final static int POINTS_COLUMN = 19;

    private final List<String> playerUsernames;
    private final Map<String, PlayerColor> playerColors;

    private final CLIText pointsTableAsset;

    public CLIPointsTable(List<String> playerUsernames, Map<String, PlayerColor> playerColors, Map<String, Integer> gamePoints, Map<String, Integer> objectivePoints) {
        this.playerUsernames = playerUsernames;
        this.playerColors = playerColors;

        List<String> pointsTableComponents = new ArrayList<>();

        pointsTableComponents.add("╔══════════════════════╗");

        for (int i = 0; i < playerUsernames.size(); i++) {
            pointsTableComponents.add("║                      ║");
        }

        pointsTableComponents.add("╚══════════════════════╝");

        this.pointsTableAsset = new CLIText(pointsTableComponents.toArray(new String[0]));

        update(gamePoints, objectivePoints);
    }

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

    public CLIText getText() {
        return this.pointsTableAsset;
    }
}
