package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.GUICardAssetRegistry;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.ResourceBundle;

public class CardController implements Initializable {

    private static final PseudoClass FUNGI = PseudoClass.getPseudoClass("card_fungi");
    private static final PseudoClass PLANT = PseudoClass.getPseudoClass("card_plant");
    private static final PseudoClass ANIMAL = PseudoClass.getPseudoClass("card_animal");
    private static final PseudoClass INSECT = PseudoClass.getPseudoClass("card_insect");

    private static final PseudoClass INTERACTABLE = PseudoClass.getPseudoClass("interactable");

    @FXML
    private StackPane cardPane;
    @FXML
    private ImageView cardImage;

    private Image front;
    private Image back;

    private SideType currSide;

    private Card card;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public ImageView getCardImage() {
        return cardImage;
    }

    public Image getImage() {
        return switch (currSide) {
            case FRONT -> front;
            case BACK -> back;
            default -> null;
        };
    }

    public void setCardAndShowSide(Card card, SideType side) {
        this.card = card;
        this.currSide = side;
        String assetPath = GUICardAssetRegistry.getAssetName(this.card.getName(), side);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(assetPath)), cardImage.getFitWidth()*1.3, cardImage.getFitHeight()*1.3, true, true);
        switch (side) {
            case FRONT -> front = image;
            case BACK -> back = image;
        }
        cardImage.setImage(image);
        cardImage.getStyleClass().remove("card_placeholder");
    }

    public void setCard(Card card) {
        this.card = card;
        String assetPathFront = GUICardAssetRegistry.getAssetName(card.getName(), SideType.FRONT);
        front = new Image(Objects.requireNonNull(getClass().getResourceAsStream(assetPathFront)), cardImage.getFitWidth()*1.3, cardImage.getFitHeight()*1.3, true, true);
        String assetPathBack = GUICardAssetRegistry.getAssetName(card.getName(), SideType.BACK);
        back = new Image(Objects.requireNonNull(getClass().getResourceAsStream(assetPathBack)), cardImage.getFitWidth()*1.3, cardImage.getFitHeight()*1.3, true, true);
    }

    public void showSide(SideType side) {
        switch (side) {
            case FRONT -> {
                if (front == null) return;
                cardImage.setImage(front);
            }
            case BACK -> {
                if (back == null) return;
                cardImage.setImage(back);
            }
            default -> {
                return;
            }
        }
        currSide = side;
        cardImage.getStyleClass().remove("card_placeholder");
    }

    public void toggleSide() {
        if (currSide == SideType.FRONT) currSide = SideType.BACK;
        else currSide = SideType.FRONT;

        showSide(currSide);
    }

    public void removeCardImage() {
        if (!cardImage.getStyleClass().contains("card_placeholder"))
            cardImage.getStyleClass().add("card_placeholder");
    }

    public void toggleInteractable() {
        boolean isInteractable = cardImage.getPseudoClassStates().contains(INTERACTABLE);
        cardImage.pseudoClassStateChanged(INTERACTABLE, !isInteractable);
    }

    public void setInteractable(boolean interactable) {
        cardImage.pseudoClassStateChanged(INTERACTABLE, interactable);
    }

    public void setShadowColor(ResourceType resourceType) {
        cardImage.pseudoClassStateChanged(FUNGI, false);
        cardImage.pseudoClassStateChanged(PLANT, false);
        cardImage.pseudoClassStateChanged(ANIMAL, false);
        cardImage.pseudoClassStateChanged(INSECT, false);
        switch (resourceType) {
            case FUNGI -> cardImage.pseudoClassStateChanged(FUNGI, true);
            case PLANT -> cardImage.pseudoClassStateChanged(PLANT, true);
            case ANIMAL -> cardImage.pseudoClassStateChanged(ANIMAL, true);
            case INSECT -> cardImage.pseudoClassStateChanged(INSECT, true);
        }
    }

    public Parent getRoot() {
        return cardPane;
    }

    public String getCardName() {
        return card.getName();
    }

    public Card getCard() {
        return card;
    }

    public void setDraggable(boolean isDraggable) {
        if (isDraggable) {
            cardPane.setOnDragDetected(e -> {
                Dragboard db = cardPane.startDragAndDrop(TransferMode.MOVE);
                Image dragImage = getImage();

                db.setDragView(dragImage, dragImage.getWidth() / 2, dragImage.getHeight() / 2);
                ClipboardContent content = new ClipboardContent();
                content.put(GUIState.draggedCard, card);
                db.setContent(content);
                e.consume();
            });

            cardPane.setOnDragDone(dragEvent -> {
                Dragboard db = dragEvent.getDragboard();
                PlayableCard playableCard = (PlayableCard) this.card;
                Position position = (Position) db.getContent(GUIState.droppedOnPos);
                try {
                    CodexGUI.getGUI().getGuiState().getServerInterface().playCard(playableCard, currSide, position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                db.clear();
                dragEvent.consume();
            });
        } else {
            cardPane.setOnDragDetected(e -> {});
        }
    }
}
