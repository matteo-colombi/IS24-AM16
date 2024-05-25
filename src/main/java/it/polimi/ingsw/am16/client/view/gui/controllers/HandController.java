package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HandController implements Initializable {

    @FXML
    private HBox root;

    private int cardAmount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardAmount = 0;
    }

    public void addCard(int i, CardController cardController) {
        root.getChildren().set(i, cardController.getRoot());
        cardAmount++;
    }

    public void addCard(CardController cardController) {;
        root.getChildren().set(cardAmount, cardController.getRoot());
        cardAmount++;
    }

    public void removeCard(PlayableCard card) {
        Node node = root.lookup("#" + card.getName());
        if (node == null) return;
        CardController placeholder = ElementFactory.getCard();
        root.getChildren().set(root.getChildren().indexOf(node), placeholder.getRoot());
        cardAmount--;
    }

    public void removeCard(RestrictedCard card) {
        Node node = root.lookup("#" + card.cardType() + ":" + card.resourceType());
        if (node == null) return;
        CardController placeholder = ElementFactory.getCard();
        root.getChildren().set(root.getChildren().indexOf(node), placeholder.getRoot());
        cardAmount--;
    }

    public Parent getRoot() {
        return root;
    }
}
