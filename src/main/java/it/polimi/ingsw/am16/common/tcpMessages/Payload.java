package it.polimi.ingsw.am16.common.tcpMessages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.am16.common.tcpMessages.request.*;
import it.polimi.ingsw.am16.common.tcpMessages.response.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "payloadType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddCardToHand.class, name = "addCardToHand"),
        @JsonSubTypes.Type(value = AddCardToOtherHand.class, name = "addCardToOtherHand"),
        @JsonSubTypes.Type(value = AddMessage.class, name = "addMessage"),
        @JsonSubTypes.Type(value = AddMessages.class, name = "addMessages"),
        @JsonSubTypes.Type(value = AddPlayer.class, name = "addPlayer"),
        @JsonSubTypes.Type(value = GetGamesResponse.class, name = "getGamesResponse"),
        @JsonSubTypes.Type(value = JoinGameResponse.class, name = "joinGameResponse"),
        @JsonSubTypes.Type(value = PlayCardResponse.class, name = "playCard"),
        @JsonSubTypes.Type(value = PromptColorChoice.class, name = "promptColorChoice"),
        @JsonSubTypes.Type(value = PromptError.class, name = "promptError"),
        @JsonSubTypes.Type(value = PromptObjectiveChoice.class, name = "promptObjectiveChoice"),
        @JsonSubTypes.Type(value = PromptStarterChoice.class, name = "promptStarterChoice"),
        @JsonSubTypes.Type(value = RemoveCardFromHand.class, name = "removeCardFromHand"),
        @JsonSubTypes.Type(value = RemoveCardFromOtherHand.class, name = "removeCardFromOtherHand"),
        @JsonSubTypes.Type(value = SetColor.class, name = "setColor"),
        @JsonSubTypes.Type(value = SetCommonCards.class, name = "setCommonCards"),
        @JsonSubTypes.Type(value = SetCommonObjectives.class, name = "setCommonObjectives"),
        @JsonSubTypes.Type(value = SetDeckTopType.class, name = "setDeckTopType"),
        @JsonSubTypes.Type(value = SetGamePoints.class, name = "setGamePoints"),
        @JsonSubTypes.Type(value = SetGameState.class, name = "setGameState"),
        @JsonSubTypes.Type(value = SetHand.class, name = "setHand"),
        @JsonSubTypes.Type(value = SetObjectivePoints.class, name = "setObjectivePoints"),
        @JsonSubTypes.Type(value = SetOtherHand.class, name = "setOtherHand"),
        @JsonSubTypes.Type(value = SetPersonalObjective.class, name = "setPersonalObjective"),
        @JsonSubTypes.Type(value = SetPlayArea.class, name = "setPlayArea"),
        @JsonSubTypes.Type(value = SetPlayers.class, name = "setPlayers"),
        @JsonSubTypes.Type(value = SetStartOrder.class, name = "setStartOrder"),
        @JsonSubTypes.Type(value = SetWinners.class, name = "setWinners"),
        @JsonSubTypes.Type(value = SignalDeadlock.class, name = "signalDeadlock"),
        @JsonSubTypes.Type(value = SignalDisconnection.class, name = "signalDisconnection"),
        @JsonSubTypes.Type(value = Turn.class, name = "turn"),
        @JsonSubTypes.Type(value = ChooseColor.class, name = "chooseColor"),
        @JsonSubTypes.Type(value = ChooseObjective.class, name = "chooseObjective"),
        @JsonSubTypes.Type(value = ChooseStarterSide.class, name = "chooseStarterSide"),
        @JsonSubTypes.Type(value = CreateGame.class, name = "createGame"),
        @JsonSubTypes.Type(value = DrawCard.class, name = "drawCard"),
        @JsonSubTypes.Type(value = JoinGameRequest.class, name = "joinGameRequest"),
        @JsonSubTypes.Type(value = PlayCardRequest.class, name = "playCardRequest"),
        @JsonSubTypes.Type(value = SendChatMessage.class, name = "sendChatMessage"),
        @JsonSubTypes.Type(value = SendPrivateChatMessage.class, name = "sendPrivateChatMessage")

})
public abstract class Payload {
}
