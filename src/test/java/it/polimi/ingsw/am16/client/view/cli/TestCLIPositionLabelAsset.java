package it.polimi.ingsw.am16.client.view.cli;

import org.junit.jupiter.api.Test;

public class TestCLIPositionLabelAsset {
    @Test
    void testCLILabelAsset() {
        CLIPositionLabelAsset label = new CLIPositionLabelAsset(-2, 3);
        label.getLabel().printText(false);
        label = new CLIPositionLabelAsset(-2, 3);

        label.getLabel().printText(false);
        label = new CLIPositionLabelAsset(-99, 99);

        label.getLabel().printText(false);
        label = new CLIPositionLabelAsset(-5, 6);

        label.getLabel().printText(false);
        label = new CLIPositionLabelAsset(8, 15);

        label.getLabel().printText(false);
        label = new CLIPositionLabelAsset(52, 49);

        label.getLabel().printText(false);
        label = new CLIPositionLabelAsset(-80, 60);

        label.getLabel().printText(false);
    }
}
