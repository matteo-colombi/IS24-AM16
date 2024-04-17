package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Map;

/**
 * DOCME
 * N.B. This class only works well with odd widths and heights, because of centering
 */
public class CLIText {
    private String[] text;
    private String[] colorMask;
    private int width;
    private int height;
    private int originX;
    private int originY;

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
        this.height = text.length;
        this.width = text.length != 0 ? text[0].length() : 0;
        this.colorMask = colorMask;
        this.originX = width / 2;
        this.originY = height / 2;
    }

    public CLIText() {
        this.text = new String[]{" "};
        this.colorMask = new String[]{" "};
        this.width = 1;
        this.height = 1;
        this.originX = 0;
        this.originY = 0;
    }

    public void mergeText(CLIText toMerge, int startRow, int startCol) {
        String[] textToMerge = toMerge.text;
        String[] colorsToMerge = toMerge.colorMask;
        int toMergeWidth = toMerge.width;
        int toMergeHeight = toMerge.height;
        if (startCol < 0) {
            expandLeft(Math.abs(startCol) + width);
            startCol = 0;
        }
        if (startRow < 0) {
            expandUp(Math.abs(startRow) + height);
            startRow = 0;
        }
        if(startCol + toMergeWidth > width) {
            expandRight(startCol + toMergeWidth);
        }
        if(startRow + toMergeHeight > height) {
            expandDown(startRow + toMergeHeight);
        }

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

    public void expandLeft(int toWidth) {
        if (toWidth < width) return;

        originX += toWidth - width;

        String expansion = spacesString(toWidth - width);
        for(int i = 0; i<text.length; i++) {
            text[i] = expansion + text[i];
            colorMask[i] = expansion + colorMask[i];
        }
        width = toWidth;
    }

    public void expandRight(int toWidth) {
        if (toWidth < width) return;

        String expansion = spacesString(toWidth - width);
        for(int i = 0; i<text.length; i++) {
            text[i] = text[i] + expansion;
            colorMask[i] = colorMask[i] + expansion;
        }
        width = toWidth;
    }

    public void expandUp(int toHeight) {
        if (toHeight < height) return;

        originY += toHeight - height;
        String expansion = spacesString(width);
        String[] newText = new String[toHeight];
        String[] newColorMask = new String[toHeight];
        for(int i = 0; i < toHeight - height; i++) {
            newText[i] = expansion;
            newColorMask[i] = expansion;
        }
        for(int i = toHeight - height; i < toHeight; i++) {
            newText[i] = text[i - toHeight + height];
            newColorMask[i] = colorMask[i - toHeight + height];
        }
        text = newText;
        colorMask = newColorMask;
        height = toHeight;
    }

    public void expandDown(int toHeight) {
        if (toHeight < height) return;
        String expansion = spacesString(width);
        String[] newText = new String[toHeight];
        String[] newColorMask = new String[toHeight];
        for(int i = 0; i < height; i++) {
            newText[i] = text[i];
            newColorMask[i] = colorMask[i];
        }
        for(int i = height; i < toHeight; i++) {
            newText[i] = expansion;
            newColorMask[i] = expansion;
        }
        text = newText;
        colorMask = newColorMask;
        height = toHeight;
    }

    public void printText(boolean frame) {
        printText(0, 0, width-1, height-1, frame);
    }

    public void printText(int startX, int startY, int endX, int endY, boolean frame) {
        startX = Math.max(startX, 0);
        startY = Math.max(startY, 0);
        endX = Math.min(endX, width-1);
        endY = Math.min(endY, height-1);
        char lastColor = ' ';
        StringBuilder toPrint = new StringBuilder();
        String horizontal = new String(new char[endX - startX + 1]).replace('\0', '─');
        String topHorizontal = '┌' + horizontal + '┐';
        char vertical = '│';
        String bottomHorizontal = '└' + horizontal + '┘';
        if (frame) {
            toPrint.append(topHorizontal).append('\n');
        }
        for(int j = startY; j <= endY; j++) {
            toPrint.append(escapeCodes.get(' ')).append(vertical).append(escapeCodes.get(lastColor));
            for(int i = startX; i <= endX; i++) {
                char thisColor = colorMask[j].charAt(i);
                if(text[j].charAt(i) != ' ' && thisColor != lastColor) {
                    toPrint.append(escapeCodes.get(thisColor));
                    lastColor = thisColor;
                }
                toPrint.append(text[j].charAt(i));
            }
            toPrint.append(escapeCodes.get(' ')).append(vertical).append(escapeCodes.get(lastColor)).append('\n');
        }
        toPrint.append(escapeCodes.get(' '));
        if (frame) {
            toPrint.append(bottomHorizontal).append('\n');
        }
        System.out.print(toPrint);
    }

    private String spacesString(int length) {
        return new String(new char[length]).replace('\0', ' ');
    }

    @Override
    public String toString() {
        return "CLIText{" +
                "text=" + Arrays.toString(text) +
                ", colorMask=" + Arrays.toString(colorMask) +
                '}';
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public CLIText getClone() {
        return new CLIText(Arrays.copyOf(this.text, this.text.length), Arrays.copyOf(this.colorMask, this.colorMask.length));
    }
}
