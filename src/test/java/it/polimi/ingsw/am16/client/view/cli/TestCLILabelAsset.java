package it.polimi.ingsw.am16.client.view.cli;

import org.junit.jupiter.api.Test;

public class TestCLILabelAsset {
    @Test
    void testCLILabelAsset() {
        CLILabelAsset label = new CLILabelAsset(-2, 3);
        label.getLabel().printText(false);
        //TODO test more
    }
}
