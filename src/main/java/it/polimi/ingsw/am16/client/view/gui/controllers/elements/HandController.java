package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller class for a player's hand GUI element. Can handle both the player's hand and other player's restricted views of their hands.
 */
public class HandController {

    @FXML
    private StackPane root;
    @FXML
    private Text placeholderText;
    @FXML
    private HBox cardsSlot;

    private List<CardController> cards;

    private String username;

    /**
     * Initializes this hand controller with an empty list of cards.
     */
    @FXML
    public void initialize() {
        cards = new ArrayList<>(3);
    }

    /**
     * Sets the username of the player whose hand element this is.
     * @param username The username of the player.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Adds a new card to this hand.
     * @param cardController The card controller for the new hand.
     */
    private void addCard(CardController cardController) {
        cards.add(cardController);
        cardsSlot.getChildren().set(cards.size()-1, cardController.getRoot());
    }

    /**
     * Adds a new playable card to this hand, setting its attributes so that it is ready to be played and has the correct hover color.
     * @param card The playable card.
     */
    public void addCard(PlayableCard card) {
        placeholderText.setVisible(false);
        CardController cardController = ElementFactory.getCard();
        cardController.getRoot().setId(card.getName());
        cardController.setCard(card);
        cardController.setShadowColor(card.getType());
        cardController.showSide(SideType.FRONT);
        cardController.setTurnable();
        addCard(cardController);
    }

    /**
     * Adds a new restricted card to this hand, setting its attributes so that it can't be turned or interacted with in any way.
     * Used for displaying other player's hands.
     * @param card The restricted card.
     */
    public void addCard(RestrictedCard card) {
        placeholderText.setVisible(false);
        CardController cardController = ElementFactory.getCardBackOnly(card.cardType(), card.resourceType());
        if (cardController == null) return;
        cardController.getRoot().setId(card.cardType() + "-" + card.resourceType());
        addCard(cardController);
    }

    /**
     * Removes a playable card from this hand.
     * @param card The card to be removed.
     */
    public void removeCard(PlayableCard card) {
        Node node = cardsSlot.lookup("#" + card.getName());
        if (node == null) return;
        CardController placeholder = ElementFactory.getCard();
        int index = cardsSlot.getChildren().indexOf(node);
        cardsSlot.getChildren().remove(node);
        cardsSlot.getChildren().addLast(placeholder.getRoot());
        cards.remove(index);
    }

    /**
     * Removes a restricted card from this hand.
     * @param card The card to be removed.
     */
    public void removeCard(RestrictedCard card) {
        Node node = root.lookup("#" + card.cardType() + "-" + card.resourceType());
        if (node == null) return;
        CardController placeholder = ElementFactory.getCard();
        int index = cardsSlot.getChildren().indexOf(node);
        cardsSlot.getChildren().remove(node);
        cardsSlot.getChildren().addLast(placeholder.getRoot());
        cards.remove(index);
    }

    /**
     * Updates what cards can be played based on their costs.
     */
    public void updateCostSatisfied() {
        Map<ResourceType, Integer> resourceCounts = CodexGUI.getGUI().getGuiState().getInfoTable(username).getResourceCounts();
        for(CardController card : cards) {
            card.updateCostSatisfied(resourceCounts);
        }
    }

    /**
     * Sets whether the cards in the hand can be played.
     * @param active Whether the cards in the hand can be played.
     */
    public void setActive(boolean active) {
        for(CardController card : cards) {
            card.setActive(active);
        }
    }

    /**
     * @return The root node of this hand element.
     */
    public Parent getRoot() {
        return root;
    }
}
