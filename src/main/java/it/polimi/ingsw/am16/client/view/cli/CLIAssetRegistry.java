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

    private CLIAssetRegistry() throws IOException {
        TypeReference<HashMap<String, CLIAsset>> cliCardsTypeRef = new TypeReference<>() {};
        File f = new File(FilePaths.CLI_CARDS);
        if (!f.exists()) {
            throw new IOException(FilePaths.CLI_CARDS + " does not exist!");
        }
        cliCards = JsonMapper.getObjectMapper().readValue(f, cliCardsTypeRef);
    }

    public static CLIAssetRegistry getCLIAssetRegistry() throws IOException {
        if (instance == null) {
            instance = new CLIAssetRegistry();
        }
        return instance;
    }

    public CLIAsset getCard(String name) {
        return cliCards.get(name);
    }
}
