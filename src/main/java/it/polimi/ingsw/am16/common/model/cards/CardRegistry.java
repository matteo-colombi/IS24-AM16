package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Singleton object that contains all the cards present in the game.
 */
public class CardRegistry {

    private static CardRegistry instance = null;

    private final ObjectMapper mapper = JsonMapper.getObjectMapper();
    private List<ObjectiveCard> objectiveCards;
    private List<StarterCard> starterCards;
    private List<GoldCard> goldCards;
    private List<ResourceCard> resourceCards;
    private Map<String, ObjectiveCard> objectiveCardsMap;
    private Map<String, StarterCard> starterCardsMap;
    private Map<String, GoldCard> goldCardsMap;
    private Map<String, ResourceCard> resourceCardsMap;

    /**
     * @return The registry instance.
     */
    public static CardRegistry getRegistry() {
        if (instance == null) {
            try {
                instance = new CardRegistry();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error initializing card registry: IOException");
            }
        }
        return instance;
    }

    /**
     * Private constructor used to create the only instance of the card registry.
     * @throws IOException Thrown if an error occurs while reading JSON files for the cards.
     */
    private CardRegistry() throws IOException {
        initializeRegistry();
    }

    /**
     * @return Whether the card registry has already been initialized.
     */
    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * Initializes the card registry with all the existing cards. The cards are taken from
     * the paths specified in {@link FilePaths}.
     * @throws IOException Thrown if an error occurs while reading JSON files for the cards.
     */
    private void initializeRegistry() throws IOException {
        initializePlayableCardsBackSides();
        initializeResourceCards();
        initializeGoldCards();
        initializeStarterCards();
        initializeObjectiveCards();
    }

    /**
     * Loads the backs of Resource and Gold cards, which are common to all these cards.
     * The JSON file is taken from {@link FilePaths}<code>.PLAYABLE_CARDS_BACK_SIDES_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private void initializePlayableCardsBackSides() throws IOException {
        try (InputStream f = CardRegistry.class.getResourceAsStream(FilePaths.PLAYABLE_CARDS_BACK_SIDES_JSON)) {
            JsonNode root = mapper.readTree(f);
            CardSide.addCommonSide("fungiBack", mapper.readValue(root.get("fungiBack").toString(), CardSide.class));
            CardSide.addCommonSide("plantBack", mapper.readValue(root.get("plantBack").toString(), CardSide.class));
            CardSide.addCommonSide("animalBack", mapper.readValue(root.get("animalBack").toString(), CardSide.class));
            CardSide.addCommonSide("insectBack", mapper.readValue(root.get("insectBack").toString(), CardSide.class));
        }
    }

    /**
     * Loads all the Resource cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.RESOURCE_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private void initializeResourceCards() throws IOException {
        try (InputStream f = CardRegistry.class.getResourceAsStream(FilePaths.RESOURCE_CARDS_JSON)) {
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
            resourceCardsMap = new HashMap<>();
            for(ResourceCard card : resourceCards) {
                resourceCardsMap.put(card.getName(), card);
            }
        }
    }

    /**
     * Loads all the Gold cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.GOLD_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private void initializeGoldCards() throws IOException {
        try (InputStream f = CardRegistry.class.getResourceAsStream(FilePaths.GOLD_CARDS_JSON)) {
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
            goldCardsMap = new HashMap<>();
            for(GoldCard card : goldCards) {
                goldCardsMap.put(card.getName(), card);
            }
        }
    }

    /**
     * Loads all the Starter cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.STARTER_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private void initializeStarterCards() throws IOException {
        try (InputStream f = CardRegistry.class.getResourceAsStream(FilePaths.STARTER_CARDS_JSON)) {
            JsonNode root = mapper.readTree(f);
            starterCards = List.of(mapper.readValue(root.get("starterCards").toString(), StarterCard[].class));
            starterCardsMap = new HashMap<>();
            for(StarterCard card : starterCards) {
                starterCardsMap.put(card.getName(), card);
            }
        }
    }

    /**
     * Loads all the Objective cards in the game.
     * The JSON file is taken from {@link FilePaths}<code>.OBJECTIVE_CARDS_JSON</code>.
     * @throws IOException If the JSON file is not found.
     */
    private void initializeObjectiveCards() throws IOException {
        try (InputStream f = CardRegistry.class.getResourceAsStream(FilePaths.OBJECTIVE_CARDS_JSON)) {
            JsonNode root = mapper.readTree(f);
            objectiveCards = List.of(mapper.readValue(root.get("objectiveCards").toString(), ObjectiveCard[].class));
            objectiveCardsMap = new HashMap<>();
            for(ObjectiveCard card : objectiveCards) {
                objectiveCardsMap.put(card.getName(), card);
            }

        }
    }

    /**
     * Returns an unmodifiable list of all the Objective Cards in the game.
     * @return An unmodifiable {@link List<ObjectiveCard>}.
     */
    public List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    /**
     * Returns an unmodifiable list of all the Starter Cards in the game.
     * @return An unmodifiable {@link List<StarterCard>}.
     */
    public List<StarterCard> getStarterCards() {
        return starterCards;
    }

    /**
     * Returns an unmodifiable list of all the Gold Cards in the game.
     * @return An unmodifiable {@link List} of {@link GoldCard}.
     */
    public List<GoldCard> getGoldCards() {
        return goldCards;
    }

    /**
     * Returns an unmodifiable list of all the Resource Cards in the game.
     * @return An unmodifiable {@link List} of {@link ResourceCard}.
     */
    public List<ResourceCard> getResourceCards() {
        return resourceCards;
    }

    /**
     *
     * @param name The name of the requested objective card.
     * @return The objective card with said name, or <code>null</code> if that objective card does not exist.
     */
    public ObjectiveCard getObjectiveCardFromName(String name) {
        return objectiveCardsMap.get(name);
    }

    /**
     *
     * @param name The name of the requested starter card.
     * @return The starter card with said name, or <code>null</code> if that starter card does not exist.
     */
    public StarterCard getStarterCardFromName(String name) {
        return starterCardsMap.get(name);
    }

    /**
     *
     * @param name The name of the requested gold card.
     * @return The gold card with said name, or <code>null</code> if that gold card does not exist.
     */
    public GoldCard getGoldCardFromName(String name) {
        return goldCardsMap.get(name);
    }

    /**
     *
     * @param name The name of the requested resource card.
     * @return The resource card with said name, or <code>null</code> if that resource card does not exist.
     */
    public ResourceCard getResourceCardFromName(String name) {
        return resourceCardsMap.get(name);
    }
}
