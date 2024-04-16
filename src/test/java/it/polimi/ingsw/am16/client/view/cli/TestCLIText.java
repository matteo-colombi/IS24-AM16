package it.polimi.ingsw.am16.client.view.cli;

import org.junit.jupiter.api.Test;

public class TestCLIText {
    @Test
    void textCLITextExpansion() {
        CLIText text = new CLIText();
        text.printText();

        text.expandDown(10);
        text.expandRight(10);

        String[] testText = {"abc", "def"};
        String[] testColorMask = {"RGB", "YPC"};

        CLIText otherText = new CLIText(testText, testColorMask);

        text.mergeText(otherText, 2, 3);

        System.out.println("---------------SEPARATOR----------------");

        text.printText();

        System.out.println("---------------SEPARATOR----------------");


        text.expandLeft(16);
        text.expandUp(12);

        text.printText();
    }
}
