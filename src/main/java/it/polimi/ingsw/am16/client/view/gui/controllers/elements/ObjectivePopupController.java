package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.List;

import static it.polimi.ingsw.am16.common.model.cards.SideType.FRONT;

/**
 * Controller for the popup that allows the player to select their personal objective.
 */
public class ObjectivePopupController {

    @FXML
    private StackPane root;
    @FXML
    private StackPane option1;
    @FXML
    private StackPane option2;

    private ObjectiveCard selectedObjective;

    private CardController option1Controller;
    private CardController option2Controller;

    private boolean done;

    /**
     * Initializes this element, preparing it for later use.
     */
    @FXML
    private void initialize() {
        option1.getChildren().clear();
        option2.getChildren().clear();
        done = false;
    }

    /**
     * Sets which objectives the player can choose from.
     * Will throw an {@link IllegalArgumentException} if there aren't at least two objectives to choose from.
     * @param possibleObjectives The objectives from which the player can choose.
     */
    public void setObjectives(List<ObjectiveCard> possibleObjectives) {
        if (possibleObjectives.size() < 2) {
            throw new IllegalArgumentException("There should be at least 2 objectives to choose from");
        }

        ObjectiveCard option1Card = possibleObjectives.get(0);
        ObjectiveCard option2Card = possibleObjectives.get(1);

        option1Controller = ElementFactory.getCard();
        option2Controller = ElementFactory.getCard();

        option1Controller.setCardAndShowSide(option1Card, FRONT);
        option2Controller.setCardAndShowSide(option2Card, FRONT);

        option1Controller.setInteractable(true);
        option2Controller.setInteractable(true);

        option1.getChildren().add(option1Controller.getRoot());
        option2.getChildren().add(option2Controller.getRoot());

        option1.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            option1Controller.setSelected(true);
            option2Controller.setSelected(false);
            option1Controller.setInteractable(false);
            option2Controller.setInteractable(true);
            selectedObjective = option1Card;
        });

        option2.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            option1Controller.setSelected(false);
            option2Controller.setSelected(true);
            option1Controller.setInteractable(true);
            option2Controller.setInteractable(false);
            selectedObjective = option2Card;
        });

    }

    /**
     * Submits an objective choice to the server.
     */
    @FXML
    private void submitObjective() {
        if (selectedObjective == null || done) return;

        try {
            CodexGUI.getGUI().getServerInterface().setPersonalObjective(selectedObjective);
        } catch (RemoteException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
            return;
        }

        done = true;
    }

    /**
     * @return The root node of this element.
     */
    public Parent getRoot() {
        return root;
    }
}
