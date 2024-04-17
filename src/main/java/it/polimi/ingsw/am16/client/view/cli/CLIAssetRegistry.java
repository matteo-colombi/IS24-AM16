package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.core.type.TypeReference;
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

        f = new File(FilePaths.CLI_POSITION_LABEL);
        if (!f.exists()) {
            throw new RuntimeException(FilePaths.CLI_POSITION_LABEL + " does not exist!");
        }
        try {
            positionLabel = JsonMapper.getObjectMapper().readValue(f, CLIText.class);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to read cli label for positions.");
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

    public CLIText getPositionLabel() {
        return positionLabel.getClone();
    }
}
