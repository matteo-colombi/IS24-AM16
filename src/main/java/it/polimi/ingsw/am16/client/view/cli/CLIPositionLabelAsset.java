package it.polimi.ingsw.am16.client.view.cli;

/**
 * A utility class to manage labels used for marking spots on a player's play area where a card can be placed.
 */
public class CLIPositionLabelAsset {

    private final CLIText label;
    private final int x;
    private final int y;

    private static final CLIText emptyLabel = new CLIText(
            new String[]{"           ", "           ", "           "}
    );

    /**
     * Creates an empty position label.
     */
    private CLIPositionLabelAsset() {
        this.label = emptyLabel;
        this.x = 0;
        this.y = 0;
    }

    /**
     * @return A blank position label. This can be used to clear previously placed position labels (for when a spot is no longer a valid place to play a card).
     */
    public static CLIPositionLabelAsset getEmptyLabel() {
        return new CLIPositionLabelAsset();
    }

    /**
     * Creates a position label containing the given coordinates.
     * @param x The label's x-coordinate.
     * @param y The label's y-coordinate.
     */
    public CLIPositionLabelAsset(int x, int y) {
        this.label = CLIAssetRegistry.getCLIAssetRegistry().getPositionLabel();
        this.x = x;
        this.y = y;
        String xLabel = String.valueOf(this.x);
        String xColor = new String(new char[xLabel.length()]).replace('\0', ' ');
        String[] xText = new String[]{xLabel};
        String[] xColorMask = new String[]{xColor};
        String yLabel = String.valueOf(this.y);
        String yColor = new String(new char[yLabel.length()]).replace('\0', ' ');
        String[] yText = new String[]{yLabel};
        String[] yColorMask = new String[]{yColor};

        CLIText newXLabel = new CLIText(xText, xColorMask);
        CLIText newYLabel = new CLIText(yText, yColorMask);

        this.label.mergeText(newXLabel, this.label.getOriginY(), this.label.getOriginX()-xLabel.length());
        this.label.mergeText(newYLabel, this.label.getOriginY(), this.label.getOriginX()+1);
    }

    /**
     * @return A print-ready {@link CLIText} representation of this position label.
     */
    public CLIText getLabel() {
        return label;
    }
}
