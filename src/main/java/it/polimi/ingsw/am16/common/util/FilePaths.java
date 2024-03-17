package it.polimi.ingsw.am16.common.util;

/**
 * Class that contains all the static paths for resources.
 */
public class FilePaths {
    /**
     * The main path for resources.
     */
    public static final String RESOURCES = "src/main/resources";

    /**
     * The path for game assets.
     */
    public static final String ASSETS = RESOURCES + "/assets";

    /**
     * The path for JSON files.
     */
    public static final String JSON = RESOURCES + "/json";

    /**
     * The path for card JSON files.
     */
    public static final String CARDS_JSON = JSON + "/cards";

    /**
     * The path for the JSON file containing the information for the front sides of all Playable Cards (Resource and Gold Cards).
     */
    public static final String PLAYABLE_CARDS_FRONT_SIDES_JSON = CARDS_JSON + "/playableCardFrontSides.json";

    /**
     * The path for the JSON file containing Resource Card information.
     */
    public static final String RESOURCE_CARDS_JSON = CARDS_JSON + "/resourceCards.json";

    /**
     * The path for the JSON file containing Gold Card information.
     */
    public static final String GOLD_CARDS_JSON = CARDS_JSON + "/goldCards.json";

    /**
     * The path for the JSON file containing the Objective Card information.
     */
    public static final String OBJECTIVE_CARDS_JSON = CARDS_JSON + "/objectiveCards.json";

    /**
     * The path for the JSON file containing the Starter Card information.
     */
    public static final String STARTER_CARDS_JSON = CARDS_JSON + "/starterCards.json";

}
