package it.polimi.ingsw.am16.client.view.cli;

import java.util.Set;

/**
 * DOCME
 */
public enum CLICommand {
    HELP(false,
            "help",
            "",
            "Prints the list of available commands."),
    GET_GAMES(
            false,
            "get_games",
            "",
            "Prints the list of available games."),
    CREATE_GAME(false,
            "create_game",
            "<username> <numPlayers>",
            "Creates a new game with the given number of players, and joins it with the given username."),
    JOIN_GAME(false,
            "join_game",
            "<username> <gameId>",
            "Joins the given game game with the given username."),
    EXIT(false,
            "exit",
            "",
            "Closes the game."),
    OBJECTIVES(true,
            "objectives",
            "",
            "Prints your personal objective and the game's common objectives."),
    DRAW_OPTIONS(true,
            "draw_options",
            "",
            "Prints the cards from which everyone can draw (decks and common cards)."),
    STARTER(true,
            "starter",
            "[front|back]",
            "Places your starter card on the specified side. If no side is given, prints your starter card."),
    COLOR(true,
            "color",
            "[color]",
            "Chooses your color. If no color is given, prints the options available.",
            Set.of("colour")),
    COMMON_OBJECTIVES(true,
            "common_objectives",
            "",
            "Prints the common objectives for this game."),
    OBJECTIVE(true,
            "objective",
            "[1|2]",
            "Sets your objective. If no index is given, prints your objective options."),
    PLAY_AREA(true,
            "play_area",
            "[username]",
            "Prints the play area of the specified player. If no username is given, prints your own play area."),
    SCROLL_VIEW(true,
            "scroll_view",
            "<left|right> <offset>",
            "Scrolls the view you have of the last printed play area in the given direction."),
    HAND(true,
            "hand",
            "[username]",
            "Prints the hand of the specified player. If no username is given, prints your own hand."),
    PLAY_CARD(true,
            "play_card",
            "<index> <front|back> <position: x,y>",
            "Plays the specified card, on the given side, in the given position."),
    DRAW_CARD(true,
            "draw_card",
            "<deck|common> <resource|gold> [(if common) <index>]",
            "Draws the specified card."),
    POINTS(true,
            "points",
            "",
            "Prints the amount of points each player has currently."),
    WINNERS(true,
            "winners",
            "",
            "Prints the usernames of the players who won this game."),
    CHAT(false,
            "chat",
            "[message]",
            "If no message is given, prints unread messages; otherwise, sends the message to the public chat."),
    CHAT_HISTORY(false,
            "chat_history",
            "",
            "Prints the whole chat history."),
    WHISPER(false,
            "whisper",
            "<receiver username> <message>",
            "Sends a private message to the player with the given username."),
    PLAYERS(false,
            "players",
            "",
            "Prints the usernames of the players in the game. If a turn order has been chosen, the usernames will be printed in the correct order."),
    ID(false,
            "id",
            "",
            "Prints the current game's id."),
    LEAVE_GAME(false,
            "leave_game",
            "",
            "Disconnects from the current game.");

    private final String command;
    private final String arguments;
    private final String description;
    private final boolean isGameCommand;
    private final Set<String> aliases;

    CLICommand(boolean isGameCommand, String command, String arguments, String description) {
        this(isGameCommand, command, arguments, description, Set.of());
    }

    CLICommand(boolean isGameCommand, String command, String arguments, String description, Set<String> aliases) {
        this.isGameCommand = isGameCommand;
        this.command = command;
        this.arguments = arguments;
        this.description = description;
        this.aliases = aliases;
    }

    public boolean isGameCommand() {
        return isGameCommand;
    }

    public boolean matches(String input) {
        return command.contains(input) || aliases.stream().anyMatch(c -> c.contains(input));
    }

    public boolean exactMatch(String input) {
        return command.equals(input);
    }

    public String getUsage() {
        return String.format("%s %s", command, arguments);
    }

    @Override
    public String toString() {
        return String.format("%s%s%s - %s", command, arguments.isEmpty() ? "" : " ", arguments, description);
    }
}
