package it.polimi.ingsw.am16.common.util;

/**
 * Class that contains all the static paths for resources.
 */
public final class FilePaths {
    private FilePaths() {}

    /**
     * The path for game assets.
     */
    public static final String ASSETS = "/assets";

    /**
     * The path for assets for the command line interface.
     */
    public static final String CLI_ASSETS = ASSETS + "/cli";

    /**
     * The path for card assets for the command line interface.
     */
    public static final String CLI_CARDS = CLI_ASSETS + "/cliCards.json";

    /**
     * The path for a placeholder blank card for the command line interface.
     */
    public static final String CLI_BLANK_CARD = CLI_ASSETS + "/cliBlankCard.json";

    /**
     * The path for the label used to signal positions in the CLI play area.
     */
    public static final String CLI_POSITION_LABEL = CLI_ASSETS + "/label.json";

    /**
     * The path for the table used to signal the amount of resources and objects in the CLI play area
     */
    public static final String CLI_INFO_TABLE = CLI_ASSETS + "/infoTable.json";

    /**
     * The path for the banner shown in the command line interface
     */
    public static final String CLI_BANNER = CLI_ASSETS + "/cliBanner.json";

    /**
     * The path for the label shown in the CLI when the final round of a game is triggered
     */
    public static final String CLI_FINAL_ROUND_LABEL = CLI_ASSETS + "/cliFinalRoundLabel.json";

    /**
     * The path for GUI assets.
     */
    public static final String GUI_ASSETS = ASSETS + "/gui";

    /**
     * The path for GUI gameplay assets, that is assets that are an integral part of the game such as cards and score board.
     */
    public static final String GUI_GAMEPLAY = GUI_ASSETS + "/gameplay";

    /**
     * The path for GUI card assets.
     */
    public static final String GUI_CARDS = GUI_GAMEPLAY + "/cards";

    /**
     * The path for the fronts of GUI cards.
     */
    public static final String GUI_CARD_FRONTS = GUI_CARDS + "/fronts";

    /**
     * The path for the backs of GUI cards.
     */
    public static final String GUI_CARD_BACKS = GUI_CARDS + "/backs";

    /**
     * The path for the GUI icons.
     */
    public static final String GUI_ICONS = GUI_ASSETS + "/icons";

    /**
     * The path for the GUI media files.
     */
    public static final String GUI_MEDIA = GUI_ASSETS + "/media";

    /**
     * The path for the GUI rulebook pages.
     */
    public static final String GUI_RULEBOOK = GUI_ASSETS + "/rulebook";

    /**
     * The path for the GUI app logo.
     */
    public static final String GUI_LOGO = GUI_ASSETS + "/logo.png";

    /**
     * The path for .fxml files.
     */
    public static final String GUI_FXML = "/fxml";

    /**
     * The path for GUI elements.
     */
    public static final String GUI_ELEMENTS = GUI_FXML + "/elements";

    /**
     * The path for GUI screens.
     */
    public static final String GUI_SCREENS = GUI_FXML + "/screens";

    /**
     * The path for JSON files.
     */
    public static final String JSON = "/json";

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
