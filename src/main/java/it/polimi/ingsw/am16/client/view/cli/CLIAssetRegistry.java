package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CLIAssetRegistry {
    private static CLIAssetRegistry instance;

    private final Map<String, CLICardAsset> cliCards;
    private final CLIText positionLabel;
    private final CLIText infoTable;
    private final CLIText banner;
    private final CLIText rick;

    private final Map<RestrictedCard, CLIText> restrictedCliCards;

    private CLIAssetRegistry() {
        TypeReference<HashMap<String, CLICardAsset>> cliCardsTypeRef = new TypeReference<>() {};
        File f = new File(FilePaths.CLI_CARDS);
        if (!f.exists()) {
            throw new RuntimeException(FilePaths.CLI_CARDS + " does not exist!");
        }
        try {
            cliCards = JsonMapper.getObjectMapper().readValue(f, cliCardsTypeRef);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to read cli cards!");
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

        f = new File(FilePaths.CLI_POSITION_LABEL);
        if (!f.exists()) {
            throw new RuntimeException(FilePaths.CLI_POSITION_LABEL + " does not exist!");
        }
        try {
            positionLabel = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to read cli label for positions.");
        }

        f = new File(FilePaths.CLI_INFO_TABLE);
        if (!f.exists()) {
            throw new RuntimeException(FilePaths.CLI_INFO_TABLE + " does not exist!");
        }
        try {
            infoTable = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to read cli info table.");
        }

        f = new File(FilePaths.CLI_BANNER);
        if (!f.exists()) {
            throw new RuntimeException(FilePaths.CLI_BANNER + " does not exist!");
        }
        try {
            banner = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to read cli banner.");
        }

        f = new File(FilePaths.CLI_ASSETS + "/rick.json");
        if (!f.exists()) {
            throw new RuntimeException("Unable to rickroll.");
        }
        try {
            rick = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to rickroll.");
        }
    }

    public static CLIAssetRegistry getCLIAssetRegistry() {
        if (instance == null) {
            instance = new CLIAssetRegistry();
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

    public CLIText getRick() {
        return rick;
    }
}
