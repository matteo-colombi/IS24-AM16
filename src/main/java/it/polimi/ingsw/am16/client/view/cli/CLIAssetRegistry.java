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

    private final Map<String, CLIAsset> cliCards;

    private CLIAssetRegistry() {
        TypeReference<HashMap<String, CLIAsset>> cliCardsTypeRef = new TypeReference<>() {};
        File f = new File(FilePaths.CLI_CARDS);
        if (!f.exists()) {
            throw new RuntimeException(FilePaths.CLI_CARDS + " does not exist!");
        }
        try {
            cliCards = JsonMapper.getObjectMapper().readValue(f, cliCardsTypeRef);
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to read cli cards!");
        }
    }

    public static CLIAssetRegistry getCLIAssetRegistry() {
        if (instance == null) {
            instance = new CLIAssetRegistry();
        }
        return instance;
    }

    public CLIAsset getCard(String name) {
        return cliCards.get(name);
    }
}
