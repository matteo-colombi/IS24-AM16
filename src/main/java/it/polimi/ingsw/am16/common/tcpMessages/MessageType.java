package it.polimi.ingsw.am16.common.tcpMessages;

public enum MessageType {
    QUIT,
    CREATE_GAME,
    JOIN_GAME,
    ADD_PLAYER,
    SET_PLAYERS,
    SET_GAME_STATE,
    SET_COMMON_CARDS,
    SET_DECK_TOP_TYPE,
    PROMPT_STARTER_CHOICE,
    CHOOSING_COLORS,
    PROMPT_COLOR_CHOICE,
    SET_COLOR,
    DRAWING_CARDS,
    SET_HAND,
    ADD_CARD_TO_HAND,
    REMOVE_CARD_FROM_HAND,
    SET_OTHER_HAND,
    ADD_CARD_TO_OTHER_HAND,
    REMOVE_CARD_FROM_OTHER_HAND,
    SET_PLAY_AREA,
    PLAY_CARD,
    SET_GAME_POINTS,
    SET_OBJECTIVE_POINTS,
    SET_COMMON_OBJECTIVES,
    PROMPT_OBJECTIVE_CHOICE,
    SET_PERSONAL_OBJECTIVE,
    SET_START_ORDER,
    TURN,
    SET_WINNERS,
    ADD_MESSAGES,
    ADD_MESSAGE,
    PROMPT_ERROR,
    REDRAW_VIEW,
    NOTIFY_DONT_DRAW,
    SIGNAL_DISCONNECTION,
    SIGNAL_DEADLOCK,
    SIGNAL_RECONNECTION,
    RICK
}
