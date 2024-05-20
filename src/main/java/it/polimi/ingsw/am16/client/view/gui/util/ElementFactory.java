package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.controllers.CardController;
import it.polimi.ingsw.am16.client.view.gui.controllers.GridFillerController;
import it.polimi.ingsw.am16.client.view.gui.controllers.PegController;
import it.polimi.ingsw.am16.client.view.gui.controllers.PlayAreaGridController;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ElementFactory {

    public static CardController getCard() {
        FXMLLoader cardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/card.fxml"));
        try {
            cardLoader.load();
            return cardLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PegController getPeg() {
        FXMLLoader pegLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/peg.fxml"));
        try {
            pegLoader.load();
            return pegLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayAreaGridController getPlayAreaGrid() {
        FXMLLoader playAreaGridLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/play-area.fxml"));
        try {
            playAreaGridLoader.load();
            return playAreaGridLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GridFillerController getGridFiller() {
        FXMLLoader gridFillerLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/grid-filler.fxml"));
        try {
            gridFillerLoader.load();
            return gridFillerLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
