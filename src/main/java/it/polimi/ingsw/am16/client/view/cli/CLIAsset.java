package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CLIAsset {
    private final CLIText front;
    private final CLIText back;

    @JsonCreator
    public CLIAsset(@JsonProperty("front") CLIText front, @JsonProperty("back") CLIText back) {
        this.front = front;
        this.back = back;
    }

    public CLIText getFront() {
        return front;
    }

    public CLIText getBack() {
        return back;
    }

    @Override
    public String toString() {
        return "CLIAsset{" +
                "front=" + front +
                ", back=" + back +
                '}';
    }
}
