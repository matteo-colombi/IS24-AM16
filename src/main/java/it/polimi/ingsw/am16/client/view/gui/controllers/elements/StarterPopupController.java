package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;

import static it.polimi.ingsw.am16.common.model.cards.SideType.BACK;
import static it.polimi.ingsw.am16.common.model.cards.SideType.FRONT;

/**
 * Controller for the popup that allows players to select on which side to play their starter card.
 */
public class StarterPopupController {

    @FXML
    private StackPane root;
    @FXML
    private StackPane frontSlot;
    @FXML
    private StackPane backSlot;

    private CardController frontController;
    private CardController backController;

    private SideType selectedSide;

    private boolean done;

    /**
     * Initializes this element, preparing it for later use.
     */
    @FXML
    public void initialize() {
        done = false;
    }

    /**
     * Submits a side choice to the server.
     */
    @FXML
    public void submitSide() {
        if (selectedSide == null || done) return;

        try {
            CodexGUI.getGUI().getServerInterface().setStarterCard(selectedSide);
        } catch (RemoteException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
            return;
        }

        done = true;
    }

    /**
     * Sets the starter card that was assigned to the player.
     * @param starterCard The starter card that was assigned to the player.
     */
    public void setStarterCard(StarterCard starterCard) {
        frontController = ElementFactory.getCard();
        backController = ElementFactory.getCard();
        frontController.setCardAndShowSide(starterCard, FRONT);
        backController.setCardAndShowSide(starterCard, BACK);
        frontController.setInteractable(true);
        backController.setInteractable(true);
        frontSlot.getChildren().set(0, frontController.getRoot());
        backSlot.getChildren().set(0, backController.getRoot());

        frontSlot.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            frontController.setSelected(true);
            backController.setSelected(false);
            frontController.setInteractable(false);
            backController.setInteractable(true);
            selectedSide = FRONT;
        });

        backSlot.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            frontController.setSelected(false);
            backController.setSelected(true);
            frontController.setInteractable(true);
            backController.setInteractable(false);
            selectedSide = BACK;
        });
    }

    /**
     * @return The root node of this element.
     */
    public Parent getRoot() {
        return root;
    }
}
