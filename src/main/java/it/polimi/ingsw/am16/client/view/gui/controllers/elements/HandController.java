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

public class HandController {

    @FXML
    private StackPane root;
    @FXML
    private Text placeholderText;
    @FXML
    private HBox cardsSlot;

    private List<CardController> cards;

    private String username;

    @FXML
    public void initialize() {
        cards = new ArrayList<>(3);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private void addCard(CardController cardController) {
        cards.add(cardController);
        cardsSlot.getChildren().set(cards.size()-1, cardController.getRoot());
    }

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

    public void addCard(RestrictedCard card) {
        placeholderText.setVisible(false);
        CardController cardController = ElementFactory.getCardBackOnly(card.cardType(), card.resourceType());
        if (cardController == null) return;
        cardController.getRoot().setId(card.cardType() + "-" + card.resourceType());
        addCard(cardController);
    }

    public void removeCard(PlayableCard card) {
        Node node = cardsSlot.lookup("#" + card.getName());
        if (node == null) return;
        CardController placeholder = ElementFactory.getCard();
        int index = cardsSlot.getChildren().indexOf(node);
        cardsSlot.getChildren().remove(node);
        cardsSlot.getChildren().addLast(placeholder.getRoot());
        cards.remove(index);
    }

    public void removeCard(RestrictedCard card) {
        Node node = root.lookup("#" + card.cardType() + "-" + card.resourceType());
        if (node == null) return;
        CardController placeholder = ElementFactory.getCard();
        int index = cardsSlot.getChildren().indexOf(node);
        cardsSlot.getChildren().remove(node);
        cardsSlot.getChildren().addLast(placeholder.getRoot());
        cards.remove(index);
    }

    public void updateCostSatisfied() {
        Map<ResourceType, Integer> resourceCounts = CodexGUI.getGUI().getGuiState().getInfoTable(username).getResourceCounts();
        for(CardController card : cards) {
            card.updateCostSatisfied(resourceCounts);
        }
    }

    public void setActive(boolean active) {
        for(CardController card : cards) {
            card.setActive(active);
        }
    }

    public Parent getRoot() {
        return root;
    }
}
