package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.model.chat.ChatModel;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;

import java.util.List;

/**
 * Interface to give external classes access to {@link Player}-type objects. It contains all the methods
 * a player is supposed to show to the outside.
 */
public interface PlayerModel extends RestrictedPlayerModel {

    @Override
    /**
     *
     * @return The player's hand, giving access only to its non-modifier methods
     */
    HandModel getHand();

    /**
     * @return The player's personal objective, or <code>null</code> if the player has yet to choose their objective.
     */
    ObjectiveCard getPersonalObjective();

    /**
     *
     * @return The player's two objective cards from which they'll choose their personal
     * objective
     */
    List<ObjectiveCard> getPersonalObjectiveOptions();

    /**
     * @return The player's starter card, regardless of whether they have already chosen the side to use.
     */
    StarterCard getStarterCard();

    /**
     *
     * @return whether the player has chosen which side of their starter card to display
     */
    boolean getChoseStarterCardSide();

    /**
     *
     * @return which of the two given possible personal objectives the player chose
     */
    boolean getChosePersonalObjective();

    /**
     * @return whether the player has already chosen their color.
     */
    boolean getChoseColor();

    /**
     * @return The player's ChatModel interface, used to interact with the lobby chat.
     */
    ChatModel getChat();

    /**
     * @return whether the player is connected.
     */
    boolean isConnected();
}

