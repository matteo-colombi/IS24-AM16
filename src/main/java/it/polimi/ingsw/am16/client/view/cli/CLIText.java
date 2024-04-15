package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;

public class CLIText {
    private final String[] text;
    private final String[] colorMask;

    private static final Map<Character, String> escapeCodes = Map.of(
            'K', (char)27 + "[30m",
            'R', (char)27 + "[31m",
            'G', (char)27 + "[32m",
            'Y', (char)27 + "[33m",
            'B', (char)27 + "[34m",
            'P', (char)27 + "[35m",
            'C', (char)27 + "[36m",
            'W', (char)27 + "[37m",
            ' ', (char)27 + "[39m"
    );

    @JsonCreator
    public CLIText(@JsonProperty("text") String[] text, @JsonProperty("color") String[] colorMask) {
        this.text = text;
        this.colorMask = colorMask;
    }

    public void mergeText(CLIText toMerge, int startRow, int startCol) {
        String[] textToMerge = toMerge.text;
        String[] colorsToMerge = toMerge.colorMask;

        for(int i = 0; i<textToMerge.length; i++) {
            int row = i + startRow;
            text[row] = text[row].substring(0, startCol)
                    + textToMerge[i]
                    + text[row].substring(startCol + textToMerge[i].length());

            colorMask[row] = colorMask[row].substring(0, startCol)
                    + colorsToMerge[i]
                    + colorMask[row].substring(startCol + colorsToMerge[i].length());
        }
    }

    public void printText() {
        char lastColor = ' ';
        StringBuilder toPrint = new StringBuilder();
        for(int j = 0; j < text.length; j++) {
            for(int i = 0; i<text[j].length(); i++) {
                char thisColor = colorMask[j].charAt(i);
                if(thisColor != lastColor) {
                    toPrint.append(escapeCodes.get(thisColor));
                    lastColor = thisColor;
                }
                toPrint.append(text[j].charAt(i));
            }
            toPrint.append('\n');
        }
        toPrint.append(escapeCodes.get(' '));
        System.out.print(toPrint);
    }

    @Override
    public String toString() {
        return "CLIText{" +
                "text=" + Arrays.toString(text) +
                ", colorMask=" + Arrays.toString(colorMask) +
                '}';
    }
}
