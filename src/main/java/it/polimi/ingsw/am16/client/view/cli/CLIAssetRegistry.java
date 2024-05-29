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

public class CLIAssetRegistry {
    private static CLIAssetRegistry instance;

    private final Map<String, CLICardAsset> cliCards;
    private final CLIText positionLabel;
    private final CLIText infoTable;
    private final CLIText banner;
    private final CLIText rick;
    private final CLIText finalRoundLabel;

    private final Map<RestrictedCard, CLIText> restrictedCliCards;

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

        try (InputStream f = CLIAssetRegistry.class.getResourceAsStream(FilePaths.CLI_ASSETS + "/rick.json")) {
            rick = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        }
    }

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

    public CLICardAsset getCard(String name) {
        return cliCards.get(name);
    }

    public CLIText getCardBack(RestrictedCard restrictedCard) {
        return restrictedCliCards.get(restrictedCard);
    }

    public CLIText getPositionLabel() {
        return positionLabel.getClone();
    }

    public CLIText getInfoTable() {
        return infoTable.getClone();
    }

    public CLIText getBanner() {
        return banner;
    }

    public CLIText getFinalRoundLabel() {
        return finalRoundLabel;
    }

    public CLIText getRick() {
        return rick;
    }
}
