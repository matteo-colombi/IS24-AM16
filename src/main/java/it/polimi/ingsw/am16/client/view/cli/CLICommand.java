package it.polimi.ingsw.am16.client.view.cli;

import java.util.Set;

/**
 * Enum that contains the types of commands available to players in the Command Line Interface.
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

    /**
     * Creates a new command for players to use in the Command Line Interface. This constructor defaults to the command having no aliases.
     * @param isGameCommand Whether the command is game-specific or relative so some other functionality (e.g. the chat).
     * @param command The command string.
     * @param arguments The arguments for the command. This is only used to print the usage of this command.
     * @param description A description of what the command does.
     */
    CLICommand(boolean isGameCommand, String command, String arguments, String description) {
        this(isGameCommand, command, arguments, description, Set.of());
    }

    /**
     * Creates a new command for players to use in the Command Line Interface.
     * @param isGameCommand Whether the command is game-specific or relative to some other functionality (e.g. the chat).
     * @param command The command string.
     * @param arguments The arguments for the command. This is only used to print the usage of this command.
     * @param description A description of what the command does.
     * @param aliases A set of aliases for this command. An alias is an alternative way of spelling the command (e.g. color and colour are both valid).
     */
    CLICommand(boolean isGameCommand, String command, String arguments, String description, Set<String> aliases) {
        this.isGameCommand = isGameCommand;
        this.command = command;
        this.arguments = arguments;
        this.description = description;
        this.aliases = aliases;
    }

    /**
     * @return Whether this command is game-specific or relative to some other functionality.
     */
    public boolean isGameCommand() {
        return isGameCommand;
    }

    /**
     * Checks whether an input matches this command or one of its aliases.
     * @param input The user's input.
     * @return <code>true</code> if the input matches this command or any of its aliases, <code>false</code> otherwise. Note that a command matches even if it's partially spelled (e.g. "a" matches "games"). It is up to the caller to check that this is the command the user actually meant to execute.
     */
    public boolean matches(String input) {
        return command.contains(input) || aliases.stream().anyMatch(c -> c.contains(input));
    }

    /**
     * Checks whether an input matches exactly this command (does not count aliases).
     * @param input The user's input.
     * @return <code>true</code> if the input exactly matches this command, <code>false</code> otherwise.
     */
    public boolean exactMatch(String input) {
        return command.equals(input);
    }

    /**
     * @return A user-friendly string to represent this command's usage.
     */
    public String getUsage() {
        return String.format("%s %s", command, arguments);
    }

    /**
     * @return A user-friendly string to represent this command's usage and its description.
     */
    @Override
    public String toString() {
        return String.format("%s%s%s - %s", command, arguments.isEmpty() ? "" : " ", arguments, description);
    }
}
