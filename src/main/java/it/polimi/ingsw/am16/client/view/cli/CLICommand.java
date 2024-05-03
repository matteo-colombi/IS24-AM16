package it.polimi.ingsw.am16.client.view.cli;

import java.util.Set;

public enum CLICommand {
    HELP("help",
            "",
            "Prints the list of available commands."),
    CREATE_GAME("create_game",
            "<username> <numPlayers>",
            "Creates a new game with the given number of players, and joins it with the given username."),
    JOIN_GAME("join_game",
            "<username> <gameId>",
            "Joins the given game game with the given username."),
    EXIT("exit",
            "",
            "Closes the game."),
    ID("id",
            "",
            "Prints the current game's id."),
    PLAYERS("players",
            "",
            "Prints the usernames of the players in the game. If a turn order has been chosen, the usernames will be printed in the correct order."),
    CHAT("chat",
            "[message]",
            "If no message is given, prints unread messages; otherwise, sends the message to the public chat."),
    CHAT_HISTORY("chat_history",
            "",
            "Prints the whole chat history."),
    WHISPER("whisper",
            "<receiver username> <message>",
            ""),
    DRAW_OPTIONS("draw_options",
            "",
            "Prints the cards from which everyone can draw (decks and common cards)."),
    COMMON_OBJECTIVES("common_objectives",
            "",
            "Prints the common objectives for this game."),
    STARTER("starter",
            "[front|back]",
            "Places your starter card on the specified side. If no side is given, prints your starter card."),
    COLOR("color",
            "[color]",
            "Chooses your color. If no color is given, prints the options available.",
            Set.of("colour")),
    OBJECTIVE("objective",
            "[1|2]",
            "Sets your objective. If no index is given, prints your objective options."),
    OBJECTIVES("objectives",
            "",
            "Prints your personal objective and the game's common objectives."),
    HAND("hand",
            "[username]",
            "Prints the hand of the specified player. If no username is given, prints your own hand."),
    PLAY_AREA("play_area",
            "[username]",
            "Prints the play area of the specified player. If no username is given, prints your own play area."),
    SCROLL_VIEW("scroll_view",
            "",
            ""),
    POINTS("points",
            "",
            "Prints the amount of points each player has currently."),
    PLAY_CARD("play_card",
            "<index> <front|back> <position: x,y>",
            "Plays the specified card, on the given side, in the given position."),
    DRAW_CARD("draw_card",
            "<deck|common> <resource|gold> [(if common) <index>]",
            "Draws the specified card."),
    WINNERS("winners",
            "",
            "Prints the usernames of the players who won this game."),
    LEAVE_GAME("leave_game",
            "",
            "Disconnects from the current game.");

    private final String command;
    private final String arguments;
    private final String description;
    private final Set<String> aliases;

    CLICommand(String command, String arguments, String description) {
        this(command, arguments, description, Set.of());
    }

    CLICommand(String command, String arguments, String description, Set<String> aliases) {
        this.command = command;
        this.arguments = arguments;
        this.description = description;
        this.aliases = aliases;
    }

    public boolean matches(String input) {
        return command.startsWith(input) || aliases.stream().anyMatch(c -> c.startsWith(input));
    }

    @Override
    public String toString() {
        return String.format("%s%s%s - %s", command, arguments.isEmpty() ? "" : " ", arguments, description);
    }
}
