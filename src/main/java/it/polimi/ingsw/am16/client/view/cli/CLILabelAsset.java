package it.polimi.ingsw.am16.client.view.cli;

public class CLILabelAsset {

    private final CLIText label;
    private final int x;
    private final int y;

    private static final CLIText emptyLabel = new CLIText(new String[]{"           ", "           ", "           "}, new String[]{"           ", "           ", "           "});

    private CLILabelAsset() {
        this.label = emptyLabel;
        this.x = 0;
        this.y = 0;
    }

    public static CLILabelAsset getEmptyLabel() {
        return new CLILabelAsset();
    }

    public CLILabelAsset(int x, int y) {
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

    public CLIText getLabel() {
        return label;
    }
}
