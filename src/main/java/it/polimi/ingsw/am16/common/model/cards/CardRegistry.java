package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.util.FilePaths;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Contains all the cards present in the game.
 */
public class CardRegistry {

    private static boolean initialized = false;
    private static List<ObjectiveCard> objectiveCards;
    private static List<StarterCard> starterCards;
    private static List<GoldCard> goldCards;
    private static List<ResourceCard> resourceCards;
    private static ObjectMapper mapper;

    private CardRegistry() {}

    /**
     * Initializes the card registry with all the existing cards. The cards are taken from
     * TODO specify where the card details are taken from.
     */
    public static boolean initializeRegistry() {
        if (initialized)
            return false;

        CornerType.bindToResourcesAndObjects();
        ObjectType.bindToCorners();
        ResourceType.bindToCorners();

        mapper = new ObjectMapper();
        try{
            initializePlayableCardsFrontSides();
            initializeResourceCards();
            initializeGoldCards();
            initializeStarterCards();
            initializeObjectiveCards();
            initialized = true;
            return true;
        } catch (IOException e) {
            //TODO Log the error
            e.printStackTrace();
            initialized = false;
            return false;
        } catch (Exception e) {
            //TODO Log the error
            e.printStackTrace();
            initialized = false;
            return false;
        }
    }

    /**
     * Loads the fronts of Resource and Gold cards, which are common to all these cards.
     * The JSON file is taken from {@link FilePaths}<code>.PLAYABLE_CARDS_FRONT_SIDES_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private static void initializePlayableCardsFrontSides() throws IOException {
        File f = new File(FilePaths.PLAYABLE_CARDS_FRONT_SIDES_JSON);
        JsonNode root = mapper.readTree(f);
        CardSide.addCommonSide("fungiFront", mapper.readValue(root.get("fungiFront").toString(), CardSide.class));
        CardSide.addCommonSide("plantFront", mapper.readValue(root.get("plantFront").toString(), CardSide.class));
        CardSide.addCommonSide("animalFront", mapper.readValue(root.get("animalFront").toString(), CardSide.class));
        CardSide.addCommonSide("insectFront", mapper.readValue(root.get("insectFront").toString(), CardSide.class));
    }

    /**
     * Loads all the Resource cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.RESOURCE_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private static void initializeResourceCards() throws IOException {
        File f = new File(FilePaths.RESOURCE_CARDS_JSON);
        JsonNode root = mapper.readTree(f);
        ResourceCard[] fungiResourceCards = mapper.readValue(root.get("fungiCards").toString(), ResourceCard[].class);
        ResourceCard[] plantResourceCards = mapper.readValue(root.get("plantCards").toString(), ResourceCard[].class);
        ResourceCard[] animalResourceCards = mapper.readValue(root.get("animalCards").toString(), ResourceCard[].class);
        ResourceCard[] insectResourceCards = mapper.readValue(root.get("insectCards").toString(), ResourceCard[].class);
        List<ResourceCard> allResourceCards = new ArrayList<>();
        allResourceCards.addAll(List.of(fungiResourceCards));
        allResourceCards.addAll(List.of(plantResourceCards));
        allResourceCards.addAll(List.of(animalResourceCards));
        allResourceCards.addAll(List.of(insectResourceCards));
        resourceCards = Collections.unmodifiableList(allResourceCards);
    }

    /**
     * Loads all the Gold cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.GOLD_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private static void initializeGoldCards() throws IOException {
        File f = new File(FilePaths.GOLD_CARDS_JSON);
        JsonNode root = mapper.readTree(f);
        GoldCard[] fungiGoldCards = mapper.readValue(root.get("fungiCards").toString(), GoldCard[].class);
        GoldCard[] plantGoldCards = mapper.readValue(root.get("plantCards").toString(), GoldCard[].class);
        GoldCard[] animalGoldCards = mapper.readValue(root.get("animalCards").toString(), GoldCard[].class);
        GoldCard[] insectGoldCards = mapper.readValue(root.get("insectCards").toString(), GoldCard[].class);
        List<GoldCard> allGoldCards = new ArrayList<>();
        allGoldCards.addAll(List.of(fungiGoldCards));
        allGoldCards.addAll(List.of(plantGoldCards));
        allGoldCards.addAll(List.of(animalGoldCards));
        allGoldCards.addAll(List.of(insectGoldCards));
        goldCards = Collections.unmodifiableList(allGoldCards);
    }

    /**
     * Loads all the Starter cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.STARTER_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private static void initializeStarterCards() throws IOException {
        File f = new File(FilePaths.STARTER_CARDS_JSON);
        JsonNode root = mapper.readTree(f);
        starterCards = List.of(mapper.readValue(root.get("starterCards").toString(), StarterCard[].class));
    }

    /**
     * Loads all the Objective cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.OBJECTIVE_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private static void initializeObjectiveCards() throws IOException {
        File f = new File(FilePaths.OBJECTIVE_CARDS_JSON);
        JsonNode root = mapper.readTree(f);
        objectiveCards = List.of(mapper.readValue(root.get("objectiveCards").toString(), ObjectiveCard[].class));
    }

    /**
     * Returns an unmodifiable list of all the Objective Cards in the game.
     * @return An unmodifiable {@link List<ObjectiveCard>}.
     */
    public static List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    /**
     * Returns an unmodifiable list of all the Starter Cards in the game.
     * @return An unmodifiable {@link List<StarterCard>}.
     */
    public static List<StarterCard> getStarterCards() {
        return starterCards;
    }

    /**
     * Returns an unmodifiable list of all the Gold Cards in the game.
     * @return An unmodifiable {@link List} of {@link GoldCard}.
     */
    public static List<GoldCard> getGoldCards() {
        return goldCards;
    }

    /**
     * Returns an unmodifiable list of all the Resource Cards in the game.
     * @return An unmodifiable {@link List} of {@link ResourceCard}.
     */
    public static List<ResourceCard> getResourceCards() {
        return resourceCards;
    }
}
