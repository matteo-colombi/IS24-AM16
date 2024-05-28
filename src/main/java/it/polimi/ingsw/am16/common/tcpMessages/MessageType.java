package it.polimi.ingsw.am16.common.tcpMessages;

/**
 * Enum that contains the types of TCP messages that can be sent, both from the server to the client, and from the client to the server.
 */
public enum MessageType {
    //Responses (from server to client)
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
    PLAY_CARD_RESPONSE,
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
    NOTIFY_DONT_DRAW,
    SIGNAL_DISCONNECTION,
    SIGNAL_GAME_SUSPENSION,
    SIGNAL_DEADLOCK,
    GET_GAMES_RESPONSE,
    JOIN_GAME_RESPONSE,
    REJOIN_INFORMATION_START,
    REJOIN_INFORMATION_END,
    PING,

    //Requests (from client to server)
    LEAVE_GAME,
    DISCONNECT,
    CHOOSE_COLOR,
    CHOOSE_OBJECTIVE,
    CHOOSE_STARTER_SIDE,
    GET_GAMES_REQUEST,
    CREATE_GAME,
    JOIN_GAME_REQUEST,
    PLAY_CARD_REQUEST,
    DRAW_CARD,
    SEND_CHAT_MESSAGE,
    SEND_PRIVATE_CHAT_MESSAGE,
    PONG
}
