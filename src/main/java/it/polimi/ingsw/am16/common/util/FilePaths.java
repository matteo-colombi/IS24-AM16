package it.polimi.ingsw.am16.common.util;

/**
 * Class that contains all the static paths for resources.
 */
public final class FilePaths {
    private FilePaths() {}
    /**
     * The main path for resources.
     */
    public static final String RESOURCES = "src/main/resources";

    /**
     * The path for game assets.
     */
    public static final String ASSETS = RESOURCES + "/assets";

    /**
     * The path for assets for the command line interface.
     */
    public static final String CLI_ASSETS = ASSETS + "/cli";

    /**
     * The path for card assets for the command line interface.
     */
    public static final String CLI_CARDS = CLI_ASSETS + "/cliCards.json";

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
    public static final String PLAYABLE_CARDS_BACK_SIDES_JSON = CARDS_JSON + "/playableCardBackSides.json";

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

    /**
     * The path for the directory where save files should be put.
     */
    public static final String SAVE_DIRECTORY = System.getProperty("user.home") + "/codexSaves";

}
