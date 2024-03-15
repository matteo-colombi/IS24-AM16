package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.exceptions.UninitializedCardRegistryException;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the cards present in the game.
 */
public class CardRegistry {
    private static boolean initialized = false;
    private static List<ObjectiveCard> objectiveCards;
    private static List<StarterCard> starterCards;
    private static List<GoldCard> goldCards;
    private static List<ResourceCard> resourceCards;

    private CardRegistry() {}

    /**
     * Initializes the card registry with all the existing cards. The cards are taken from
     * TODO specify where the card details are taken from.
     */
    public static void initializeRegistry() {
        objectiveCards = new ArrayList<>();
        starterCards = new ArrayList<>();
        goldCards = new ArrayList<>();
        resourceCards = new ArrayList<>();

        initialized = true;
    }

    private static void initializeObjectiveCards() {

    }

    private static void initializeStarterCards() {

    }

    private static void initializeGoldCards() {

    }

    private static void initializeResourceCards() {

    }

    /**
     * Returns an unmodifiable list of all the Objective Cards in the game.
     * @return An unmodifiable {@link List<ObjectiveCard>}.
     */
    public static List<ObjectiveCard> getObjectiveCards() {
        if (!initialized)
            throw new UninitializedCardRegistryException("The card registry must be initialized");
        return objectiveCards;
    }

    /**
     * Returns an unmodifiable list of all the Starter Cards in the game.
     * @return An unmodifiable {@link List<StarterCard>}.
     */
    public static List<StarterCard> getStarterCards() {
        if (!initialized)
            throw new UninitializedCardRegistryException("The card registry must be initialized");
        return starterCards;
    }

    /**
     * Returns an unmodifiable list of all the Gold Cards in the game.
     * @return An unmodifiable {@link List<GoldCard>}.
     */
    public static List<GoldCard> getGoldCards() {
        if (!initialized)
            throw new UninitializedCardRegistryException("The card registry must be initialized");
        return goldCards;
    }

    /**
     * Returns an unmodifiable list of all the Resource Cards in the game.
     * @return An unmodifiable {@link List<ResourceCard>}.
     */
    public static List<ResourceCard> getResourceCards() {
        if (!initialized)
            throw new UninitializedCardRegistryException("The card registry must be initialized");
        return resourceCards;
    }
}
