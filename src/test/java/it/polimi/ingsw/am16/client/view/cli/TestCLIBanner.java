package it.polimi.ingsw.am16.client.view.cli;

import org.junit.jupiter.api.Test;

public class TestCLIBanner {
    @Test
    void testCLIBanner() {
        CLIAssetRegistry.getCLIAssetRegistry().getBanner().printText(true);
    }
}
