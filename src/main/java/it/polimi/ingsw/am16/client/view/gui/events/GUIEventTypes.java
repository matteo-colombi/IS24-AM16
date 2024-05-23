package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.EventType;

public class GUIEventTypes {

    public static final EventType<ErrorEvent> ERROR_EVENT = new EventType<>("ERROR_EVENT");

    public static final EventType<SetGamesListEvent> SET_GAMES_LIST_EVENT = new EventType<>("GAMES_LIST_EVENT");

    public static final EventType<AddPlayerEvent> ADD_PLAYER_EVENT = new EventType<>("ADD_PLAYER_EVENT");

    public static final EventType<SetPlayersEvent> SET_PLAYERS_EVENT = new EventType<>("SET_PLAYERS_EVENT");

    public static final EventType<SetGameStateEvent> SET_GAME_STATE_EVENT = new EventType<>("SET_GAME_STATE_EVENT");

    public static final EventType<SetCommonCardsEvent> SET_COMMON_CARDS_EVENT = new EventType<>("SET_COMMON_CARDS_EVENT");

    public static final EventType<SetDeckTopTypeEvent> SET_DECK_TOP_TYPE_EVENT = new EventType<>("SET_DECK_TOP_TYPE_EVENT");

    public static final EventType<PromptStarterChoiceEvent> PROMPT_STARTER_CHOICE_EVENT = new EventType<>("PROMPT_STARTER_CHOICE_EVENT");

    public static final EventType<ChoosingColorsEvent> CHOOSING_COLORS_EVENT = new EventType<>("CHOOSING_COLORS_EVENT");

    public static final EventType<PromptColorChoiceEvent> PROMPT_COLOR_CHOICE_EVENT = new EventType<>("PROMPT_COLOR_CHOICE_EVENT");

    public static final EventType<SetColorEvent> SET_COLOR_EVENT = new EventType<>("SET_COLOR_EVENT");

    public static final EventType<DrawingCardsEvent> DRAWING_CARDS_EVENT = new EventType<>("DRAWING_CARDS_EVENT");

    public static final EventType<SetHandEvent> SET_HAND_EVENT = new EventType<>("SET_HAND_EVENT");

    public static final EventType<AddCardToHandEvent> ADD_CARD_TO_HAND_EVENT = new EventType<>("ADD_CARD_TO_HAND_EVENT");

    public static final EventType<RemoveCardFromHandEvent> REMOVE_CARD_FROM_HAND_EVENT = new EventType<>("REMOVE_CARD_FROM_HAND_EVENT");

    public static final EventType<SetOtherHandEvent> SET_OTHER_HAND_EVENT = new EventType<>("SET_OTHER_HAND_EVENT");

    public static final EventType<AddCardToOtherHandEvent> ADD_CARD_TO_OTHER_HAND_EVENT = new EventType<>("ADD_CARD_TO_OTHER_HAND_EVENT");

    public static final EventType<RemoveCardFromOtherHandEvent> REMOVE_CARD_FROM_OTHER_HAND_EVENT = new EventType<>("REMOVE_CARD_FROM_OTHER_HAND_EVENT");

    public static final EventType<SetPlayAreaEvent> SET_PLAY_AREA_EVENT = new EventType<>("SET_PLAY_AREA_EVENT");

    public static final EventType<PlayCardEvent> PLAY_CARD_EVENT = new EventType<>("PLAY_CARD_EVENT");

    public static final EventType<SetGamePointsEvent> SET_GAME_POINTS_EVENT = new EventType<>("SET_GAME_POINTS_EVENT");

    public static final EventType<SetObjectivePointsEvent> SET_OBJECTIVE_POINTS_EVENT = new EventType<>("SET_OBJECTIVE_POINTS_EVENT");

    public static final EventType<SetCommonObjectivesEvent> SET_COMMON_OBJECTIVES_EVENT = new EventType<>("SET_COMMON_OBJECTIVES_EVENT");

    public static final EventType<PromptObjectiveChoiceEvent> PROMPT_OBJECTIVE_CHOICE_EVENT = new EventType<>("PROMPT_OBJECTIVE_CHOICE_EVENT");

    public static final EventType<SetPersonalObjectiveEvent> SET_PERSONAL_OBJECTIVE_EVENT = new EventType<>("SET_PERSONAL_OBJECTIVE_EVENT");

    public static final EventType<SetStartOrderEvent> SET_START_ORDER_EVENT = new EventType<>("SET_START_ORDER_EVENT");

    public static final EventType<TurnEvent> TURN_EVENT = new EventType<>("TURN_EVENT");

    public static final EventType<SetWinnersEvent> SET_WINNERS_EVENT = new EventType<>("SET_WINNERS_EVENT");

    public static final EventType<AddChatMessagesEvent> ADD_CHAT_MESSAGES_EVENT = new EventType<>("ADD_CHAT_MESSAGES_EVENT");

    public static final EventType<NotifyDontDrawEvent> NOTIFY_DONT_DRAW_EVENT = new EventType<>("NOTIFY_DONT_DRAW_EVENT");

    public static final EventType<SignalDisconnectionEvent> SIGNAL_DISCONNECTION_EVENT = new EventType<>("SIGNAL_DISCONNECTION_EVENT");

    public static final EventType<SignalGameSuspensionEvent> SIGNAL_GAME_SUSPENSION_EVENT = new EventType<>("SIGNAL_GAME_SUSPENSION_EVENT");

    public static final EventType<SignalDeadlockEvent> SIGNAL_DEADLOCK_EVENT = new EventType<>("SIGNAL_DEADLOCK_EVENT");
}
