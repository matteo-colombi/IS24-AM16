package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton utility class that loads assets for a CLI client.
 */
public class CLIAssetRegistry {

    private static CLIAssetRegistry instance;

    private final Map<String, CLICardAsset> cliCards;
    private final CLIText blankCard;
    private final CLIText positionLabel;
    private final CLIText infoTable;
    private final CLIText banner;
    private final CLIText finalRoundLabel;

    private final Map<RestrictedCard, CLIText> restrictedCliCards;

    /**
     * Constructor that starts the registry and loads all assets.
     * @throws IOException Thrown if any asset could not be loaded.
     */
    private CLIAssetRegistry() throws IOException {
        TypeReference<HashMap<String, CLICardAsset>> cliCardsTypeRef = new TypeReference<>() {};
        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_CARDS)) {
            cliCards = JsonMapper.getObjectMapper().readValue(f, cliCardsTypeRef);
        }

        restrictedCliCards = new HashMap<>();
        for(PlayableCardType cardType : PlayableCardType.values()) {
            for(ResourceType resourceType : ResourceType.values()) {
                restrictedCliCards.put(
                        new RestrictedCard(cardType, resourceType),
                        cliCards.get(String.format("%s_%s_1", cardType.name().toLowerCase(), resourceType.name().toLowerCase())).back()
                );
            }
        }

        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_BLANK_CARD)) {
            blankCard = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        }

        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_POSITION_LABEL)) {
            positionLabel = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        }

        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_INFO_TABLE)) {
            infoTable = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        }

        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_BANNER)) {
            banner = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        }

        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_FINAL_ROUND_LABEL)) {
            finalRoundLabel = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        }
    }

    /**
     * @return The only {@link CLIAssetRegistry} instance.
     */
    public static CLIAssetRegistry getCLIAssetRegistry() {
        if (instance == null) {
            try {
                instance = new CLIAssetRegistry();
            } catch (IOException e) {
                System.out.println("Unable to initiate CLI asset registry: " + e.getMessage());
            }
        }
        return instance;
    }

    /**
     * Retrieves a card asset from the registry.
     * @param name The name of the card.
     * @return The asset of the given card, or null if the card does not exist.
     */
    public CLICardAsset getCard(String name) {
        return cliCards.get(name);
    }

    /**
     * Retrieves the asset for the back of a card from the registry.
     * @param restrictedCard The restricted view of the card for which the back should be retrieved.
     * @return The requested asset.
     */
    public CLIText getCardBack(RestrictedCard restrictedCard) {
        return restrictedCliCards.get(restrictedCard);
    }

    /**
     * @return An empty position label for use in play areas.
     */
    public CLIText getPositionLabel() {
        return positionLabel.getClone();
    }

    /**
     * @return An empty info table.
     */
    public CLIText getInfoTable() {
        return infoTable.getClone();
    }

    /**
     * @return The CLI banner, used when starting the game.
     */
    public CLIText getBanner() {
        return banner;
    }

    /**
     * @return The final round colored text label.
     */
    public CLIText getFinalRoundLabel() {
        return finalRoundLabel;
    }

    public CLIText getBlankCard() {
        return blankCard;
    }
}
