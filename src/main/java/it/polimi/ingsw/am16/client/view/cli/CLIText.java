package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 *  Utility class to store and manage colored Command Line Interface Assets.
 *  A {@link CLIText} should be thought of as a matrix of characters where each character can have its own color.
 *  Please note that this class works best with odd widths and heights because assets with odd lengths have a well-defined center.
 * </p>
 * <p>
 *  This class only works well if ANSI color escape codes are available on the terminal that is used to run the CLI; behavior is otherwise unspecified (it might work without the colors, or it might print seemingly random characters).
 * </p>
 */
public class CLIText {
    private String[] text;
    private String[] colorMask;
    private int width;
    private int height;
    private int originX;
    private int originY;

    private static final Map<Character, String> escapeCodes = Map.of(
            'K', (char) 27 + "[30m",
            'R', (char) 27 + "[31m",
            'G', (char) 27 + "[32m",
            'Y', (char) 27 + "[33m",
            'B', (char) 27 + "[34m",
            'P', (char) 27 + "[35m",
            'C', (char) 27 + "[36m",
            ' ', (char) 27 + "[39m"
    );

    /**
     * Creates a new Command Line Interface asset with the given text and color mask. The two arrays given should have the same length, and every string in the arrays should also have the same length.
     * @param text An array containing each of the text's rows.
     * @param colorMask An array containing the color mask of each of the text's rows. The available colors are:
     *                  <ul>
     *                      <li>K for black;</li>
     *                      <li>R for red;</li>
     *                      <li>G for green;</li>
     *                      <li>Y for yellow;</li>
     *                      <li>B for blue;</li>
     *                      <li>P for purple;</li>
     *                      <li>C for cyan;</li>
     *                      <li>A blank space for the terminal's default text color.</li>
     *                  </ul>
     */
    @JsonCreator
    public CLIText(@JsonProperty("text") String[] text, @JsonProperty("color") String[] colorMask) {
        this.text = text;
        this.height = text.length;
        this.width = text.length != 0 ? text[0].length() : 0;
        this.colorMask = colorMask;
        this.originX = width / 2;
        this.originY = height / 2;
    }

    /**
     * Creates a new single-line, all white {@link CLIText}. This is useful when a line of text will have to be merged with a more complex {@link CLIText}.
     * @param text The single line's content.
     */
    public CLIText(String text) {
        this(new String[]{text});
    }

    /**
     * Creates a new single-link text that is colored with the given {@link PlayerColor}.
     * @param text The single line's content.
     * @param playerColor The color to color this text with.
     */
    public CLIText(String text, PlayerColor playerColor) {
        this(text);
        this.colorMask[0] = this.colorMask[0].replace(' ', playerColorToChar(playerColor));
    }

    /**
     * Creates a new, all white {@link CLIText}. This is useful when colors are not needed, such as when creating position labels.
     * @param text An array containing each of the text's rows.
     */
    public CLIText(String[] text) {
        this(text, new String[text.length]);
        Arrays.fill(this.colorMask, new String(new char[text[0].length()]).replace('\0', ' '));
    }

    /**
     * Creates a new, 1x1 empty CLIText. Useful when contents will be merged later on.
     */
    public CLIText() {
        this.text = new String[]{" "};
        this.colorMask = new String[]{" "};
        this.width = 1;
        this.height = 1;
        this.originX = 0;
        this.originY = 0;
    }

    /**
     * Merges the given asset into this asset, replacing this asset's contents when the two overlap.
     * This method also expands this asset as needed to fit the new asset.
     * @param toMerge The asset to merge into this asset.
     * @param startRow The row where the top-left corner of the new asset should go.
     * @param startCol The column where the top-left corner of the new asset should go.
     */
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
        if (startCol + toMergeWidth > width) {
            expandRight(startCol + toMergeWidth);
        }
        if (startRow + toMergeHeight > height) {
            expandDown(startRow + toMergeHeight);
        }

        for (int i = 0; i < textToMerge.length; i++) {
            int row = i + startRow;
            text[row] = text[row].substring(0, startCol)
                    + textToMerge[i]
                    + text[row].substring(startCol + textToMerge[i].length());

            colorMask[row] = colorMask[row].substring(0, startCol)
                    + colorsToMerge[i]
                    + colorMask[row].substring(startCol + colorsToMerge[i].length());
        }
    }

    /**
     * Expands this asset to the left with blank spaces up to the specified width (for example, if the asset is currently 5 characters wide, passing 7 as a target width will add 2 blank spaces to the left of every line).
     * @param toWidth The width to expand this asset to.
     */
    public void expandLeft(int toWidth) {
        if (toWidth < width) return;

        originX += toWidth - width;

        String expansion = spacesString(toWidth - width);
        for (int i = 0; i < text.length; i++) {
            text[i] = expansion + text[i];
            colorMask[i] = expansion + colorMask[i];
        }
        width = toWidth;
    }

    /**
     * Expands this asset to the right with blank spaces up to the specified width (for example, if the asset is currently 5 characters wide, passing 7 as a target width will add 2 blank spaces to the right of every line).
     * @param toWidth The width to expand this asset to.
     */
    public void expandRight(int toWidth) {
        if (toWidth < width) return;

        String expansion = spacesString(toWidth - width);
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i] + expansion;
            colorMask[i] = colorMask[i] + expansion;
        }
        width = toWidth;
    }

    /**
     * Expands this asset upwards with blank spaces up to the specified height (for example, if the asset is currently 5 characters high, passing 7 as a target height will add 2 blank spaces to the top of every column).
     * @param toHeight The height to expand this asset to.
     */
    public void expandUp(int toHeight) {
        if (toHeight < height) return;

        originY += toHeight - height;
        String expansion = spacesString(width);
        String[] newText = new String[toHeight];
        String[] newColorMask = new String[toHeight];
        for (int i = 0; i < toHeight - height; i++) {
            newText[i] = expansion;
            newColorMask[i] = expansion;
        }
        for (int i = toHeight - height; i < toHeight; i++) {
            newText[i] = text[i - toHeight + height];
            newColorMask[i] = colorMask[i - toHeight + height];
        }
        text = newText;
        colorMask = newColorMask;
        height = toHeight;
    }

    /**
     * Expands this asset downwards with blank spaces up to the specified height (for example, if the asset is currently 5 characters high, passing 7 as a target height will add 2 blank spaces to the bottom of every column).
     * @param toHeight The height to expand this asset to.
     */
    public void expandDown(int toHeight) {
        if (toHeight < height) return;
        String expansion = spacesString(width);
        String[] newText = new String[toHeight];
        String[] newColorMask = new String[toHeight];
        for (int i = 0; i < height; i++) {
            newText[i] = text[i];
            newColorMask[i] = colorMask[i];
        }
        for (int i = height; i < toHeight; i++) {
            newText[i] = expansion;
            newColorMask[i] = expansion;
        }
        text = newText;
        colorMask = newColorMask;
        height = toHeight;
    }

    /**
     * Prints this text to {@link System#out} without a bordering frame.
     */
    public void printText() {
        printText(false);
    }

    /**
     * Prints this text to {@link System#out}.
     * @param frame Whether to add a thin frame around the printed text.
     */
    public void printText(boolean frame) {
        CLIText tmpText = this;

        if (frame)
            tmpText = this.addFrame();

        System.out.println(tmpText.color());
    }

    /**
     * Creates a copy of this {@link CLIText} with a frame added all around it.
     * @return The new asset with the frame added.
     */
    public CLIText addFrame() {
        return addFrame(0, 0, width - 1, height - 1);
    }

    /**
     * Creates a copy of this {@link CLIText} with a frame around the specified area.
     * @param startX The frame's top-left corner's x-coordinate (the column).
     * @param startY The frame's top-left corner's y-coordinate (the row).
     * @param endX The frame's bottom-right corner's x-coordinate (the column).
     * @param endY The frame's bottom-right corner's y-coordinate (the row).
     * @return The new asset with the frame added.
     */
    public CLIText addFrame(int startX, int startY, int endX, int endY) {
        startX = Math.max(startX, 0);
        startY = Math.max(startY, 0);
        endX = Math.min(endX, width - 1);
        endY = Math.min(endY, height - 1);

        StringBuilder framedText = new StringBuilder();
        StringBuilder framedColorMask = new StringBuilder();

        String horizontal = new String(new char[endX - startX + 5]).replace('\0', '─');
        String topHorizontal = '┌' + horizontal + '┐';
        String bottomHorizontal = '└' + horizontal + '┘';

        char vertical = '│';
        String startVertical = vertical + "  ";
        String endVertical = "  " + vertical;

        String horizontalMask = " " + new String(new char[endX - startX + 5]).replace('\0', ' ') + " ";
        String startVerticalMask = " " + "  ";
        String endVerticalMask = "  " + " ";

        framedText.append(topHorizontal).append('\n');
        framedColorMask.append(horizontalMask).append('\n');

        for (int j = startY; j <= endY; j++) {
            framedText.append(startVertical);
            framedColorMask.append(startVerticalMask);

            for (int i = startX; i <= endX; i++) {
                framedText.append(text[j].charAt(i));
                framedColorMask.append(colorMask[j].charAt(i));
            }

            framedText.append(endVertical).append('\n');
            framedColorMask.append(endVerticalMask).append('\n');
        }

        framedText.append(bottomHorizontal).append('\n');
        framedColorMask.append(horizontalMask).append('\n');

        return new CLIText(framedText.toString().split("\n"), framedColorMask.toString().split("\n"));
    }

    /**
     * Injects the ANSI color escape codes into the asset and unifies the whole asset into a single string.
     * @return A print-ready version of the asset (including new-line characters).
     */
    private String color() {
        StringBuilder coloredText = new StringBuilder();

        for (int j = 0; j <= height - 1; j++) {
            for (int i = 0; i <= width - 1; i++) {
                char character = text[j].charAt(i);
                char color = colorMask[j].charAt(i);

                coloredText.append(escapeCodes.get(color)).append(character);
            }

            if (j != height - 1)
                coloredText.append("\n");
        }

        coloredText.append(escapeCodes.get(' '));

        return coloredText.toString();
    }

    /**
     * Creates a string of spaces of the specified length.
     * @param length The length of the string to create.
     * @return The created string of empty spaces.
     */
    private String spacesString(int length) {
        return new String(new char[length]).replace('\0', ' ');
    }

    /**
     * @return A string representation of this asset for debug purposes. Use the {@link CLIText#printText} method and its overloads for actual printing.
     */
    @Override
    public String toString() {
        return "CLIText{" +
                "text=" + Arrays.toString(text) +
                ", colorMask=" + Arrays.toString(colorMask) +
                '}';
    }

    /**
     * @return This asset's current width. Can change if it is expanded via merging new assets or calling the {@link CLIText#expandLeft} or {@link CLIText#expandRight} methods.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return This asset's current height. Can change if it is expanded via merging new assets or calling the {@link CLIText#expandUp} or {@link CLIText#expandDown} methods.
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The x-coordinate of this asset's origin. The origin is the point where the asset started, before anything was merged into it.
     */
    public int getOriginX() {
        return originX;
    }

    /**
     * @return The y-coordinate of this asset's origin. The origin is the point where the asset started, before anything was merged into it.
     */
    public int getOriginY() {
        return originY;
    }

    /**
     * @return A deep copy of this {@link CLIText}.
     */
    public CLIText getClone() {
        return new CLIText(Arrays.copyOf(this.text, this.text.length), Arrays.copyOf(this.colorMask, this.colorMask.length));
    }

    /**
     * Extracts a part of this text, useful when only a portion should be printed.
     * @param startX The x-coordinate of the top-left corner of the rectangular portion that should be extracted.
     * @param startY The y-coordinate of the top-left corner of the rectangular portion that should be extracted.
     * @param endX The x-coordinate of the bottom-right corner of the rectangular portion that should be extracted.
     * @param endY The y-coordinate of the bottom-right corner of the rectangular portion that should be extracted.
     * @return The extracted portion.
     */
    public CLIText getSubText(int startX, int startY, int endX, int endY) {
        startX = Math.max(startX, 0);
        startY = Math.max(startY, 0);
        endX = Math.min(endX, width - 1);
        endY = Math.min(endY, height - 1);

        int newHeight = endY - startY + 1;

        String[] newText = new String[newHeight];
        String[] newColorMask = new String[newHeight];

        for (int i = 0; i < newHeight; i++) {
            newText[i] = text[i + startY].substring(startX, endX + 1);
            newColorMask[i] = colorMask[i + startY].substring(startX, endX + 1);
        }

        return new CLIText(newText, newColorMask);
    }

    /**
     * Utility method to convert from {@link PlayerColor} to a character color code used by this class in the color masks.
     * @param playerColor The player color.
     * @return The character color code equivalent to the given player color.
     */
    public char playerColorToChar(PlayerColor playerColor) {
        if (playerColor == null) return ' ';
        return switch (playerColor) {
            case RED -> 'R';
            case BLUE -> 'B';
            case GREEN -> 'G';
            case YELLOW -> 'Y';
        };
    }
}
