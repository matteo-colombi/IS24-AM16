package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.GUIAssetRegistry;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Objects;

/**
 * Controller class for card assets in the Graphical User Interface.
 */
public class CardController {
    @FXML
    private StackPane cardPane;
    @FXML
    private ImageView cardImage;
    @FXML
    private ImageView turnImage;

    private Image front;
    private Image back;

    private SideType currSide;

    private Card card;

    private DrawType drawType;

    private Map<ResourceType, Integer> cost;
    private boolean costSatisfied;
    private boolean active;

    /**
     * Prepares the element for later use.
     */
    @FXML
    private void initialize() {
        costSatisfied = true;
    }

    /**
     * @return The image currently displayed in this card.
     */
    public Image getImage() {
        return switch (currSide) {
            case FRONT -> front;
            case BACK -> back;
        };
    }

    /**
     * Loads the card image for the specified card and side and inserts them in the GUI element.
     * @param card The card to be displayed.
     * @param side The requested side.
     */
    public void setCardAndShowSide(Card card, SideType side) {
        this.card = card;
        this.currSide = side;
        String assetPath = GUIAssetRegistry.getAssetName(this.card.getName(), side);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(assetPath)), cardImage.getFitWidth()*1.3, cardImage.getFitHeight()*1.3, true, true);
        switch (side) {
            case FRONT -> front = image;
            case BACK -> back = image;
        }
        cardImage.setImage(image);
        cardImage.getStyleClass().remove("card_placeholder");
    }

    /**
     * Loads the image for both sides of the given card, without actually inserting it in the GUI element. Use {@link CardController#showSide} to actually display a side of the card.
     * @param card The card to be displayed.
     */
    public void setCard(Card card) {
        this.card = card;
        String assetPathFront = GUIAssetRegistry.getAssetName(card.getName(), SideType.FRONT);
        front = new Image(Objects.requireNonNull(getClass().getResourceAsStream(assetPathFront)), cardImage.getFitWidth()*1.3, cardImage.getFitHeight()*1.3, true, true);
        String assetPathBack = GUIAssetRegistry.getAssetName(card.getName(), SideType.BACK);
        back = new Image(Objects.requireNonNull(getClass().getResourceAsStream(assetPathBack)), cardImage.getFitWidth()*1.3, cardImage.getFitHeight()*1.3, true, true);
    }

    /**
     * Loads the image for both sides of the given playable card. Also extracts the card's cost. Like {@link CardController#setCard(Card)}, this method does not insert the card image in the GUI element. Use {@link CardController#showSide} to display a side of the card.
     * @param card The playable card to be displayed.
     */
    public void setCard(PlayableCard card) {
        this.cost = card.getFrontSide().getCost();
        setCard((Card) card);
    }

    /**
     * Shows one side of the loaded card. If {@link CardController#setCard} was not called, this method does nothing.
     * @param side The side to show.
     */
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

    /**
     * Toggles which side of the card is shown.
     */
    public void toggleSide() {
        if (currSide == SideType.FRONT) {
            currSide = SideType.BACK;
        } else {
            currSide = SideType.FRONT;
        }
        updateDisabled();
        updateActive();
        showSide(currSide);
    }

    /**
     * Sets whether this card is active. Only active cards can be dragged and display a shadow when hovered over.
     * @param active The active flag.
     */
    public void setActive(boolean active) {
        this.active = active;
        updateActive();
    }

    /**
     * Updates the style classes on the cards after a change in the active flag.
     */
    public void updateActive() {
        boolean ok = active && (currSide == SideType.BACK || costSatisfied);
        setInteractable(ok);
        setDraggable(ok);
    }

    /**
     * Sets the style class for whether this card should have a hover shadow effect.
     * @param interactable The interactable flag.
     */
    public void setInteractable(boolean interactable) {
        if (interactable) {
            if (!cardImage.getStyleClass().contains("interactable")) {
                cardImage.getStyleClass().add("interactable");
            }
        } else {
            cardImage.getStyleClass().remove("interactable");
        }
    }

    /**
     * Sets whether this card is selected. A card with the <code>selected</code> style class has a shadow effect.
     * @param selected The selected flag.
     */
    public void setSelected(boolean selected) {
        if (selected) {
            if (!cardImage.getStyleClass().contains("selected")) {
                cardImage.getStyleClass().add("selected");
            }
        } else {
            cardImage.getStyleClass().remove("selected");
        }
    }

    /**
     * Updates whether this card should be active based on whether its cost is satisfied.
     * @param resourceCounts A map containing the amount of every resource currently visible on the player's play area.
     */
    public void updateCostSatisfied(Map<ResourceType, Integer> resourceCounts) {
        costSatisfied = true;
        for(ResourceType resourceType : cost.keySet()) {
            if (cost.get(resourceType) > resourceCounts.getOrDefault(resourceType, 0)) {
                costSatisfied = false;
                break;
            }
        }
        updateDisabled();
        updateActive();
    }

    /**
     * Updates whether this card should be disabled. A <code>disabled</code> card is semi-transparent.
     */
    public void updateDisabled() {
        if (currSide == SideType.FRONT && !costSatisfied) {
            if (!cardPane.getStyleClass().contains("disabled")) {
                cardPane.getStyleClass().add("disabled");
            }
        } else {
            cardPane.getStyleClass().remove("disabled");
        }
    }

    /**
     * Allows this card to be turned by clicking on it.
     */
    public void setTurnable() {
        cardPane.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            toggleSide();
            mouseEvent.consume();
        });

        cardPane.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> turnImage.setVisible(true));

        cardPane.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> turnImage.setVisible(false));
    }

    /**
     * Sets the color of the hover shadow for this card.
     * @param resourceType The resource type of the card, which determines its color.
     */
    public void setShadowColor(ResourceType resourceType) {
        cardImage.getStyleClass().remove("card_fungi");
        cardImage.getStyleClass().remove("card_plant");
        cardImage.getStyleClass().remove("card_animal");
        cardImage.getStyleClass().remove("card_insect");

        switch (resourceType) {
            case FUNGI -> cardImage.getStyleClass().add("card_fungi");
            case PLANT -> cardImage.getStyleClass().add("card_plant");
            case ANIMAL -> cardImage.getStyleClass().add("card_animal");
            case INSECT -> cardImage.getStyleClass().add("card_insect");
        }
    }

    /**
     * @return The root element of this card element.
     */
    public Parent getRoot() {
        return cardPane;
    }

    /**
     * @return Which card is displayed in this card GUI element.
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets whether this card can be dragged.
     * @param isDraggable Whether this card can be dragged.
     */
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
                if (position != null) {
                    try {
                        CodexGUI.getGUI().getGuiState().getServerInterface().playCard(playableCard, currSide, position);
                    } catch (RemoteException e) {
                        System.err.println("Error communicating with the server: " + e.getMessage());
                    }
                }
                db.clear();
                dragEvent.consume();
            });
        } else {
            cardPane.setOnDragDetected(e -> {});
        }
    }

    /**
     * Sets the type of draw for this card. Should only be set for cards that are in the draw options.
     * @param drawType The draw type for this card (one of the decks or one of the 4 common cards).
     */
    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }

    /**
     * Sets whether this card is in the draw options.
     * @param drawable Whether this card is in the draw options.
     */
    public void setDrawable(boolean drawable) {
        if (drawable) {
            cardPane.setOnMouseClicked(mouseEvent -> {
                try {
                    CodexGUI.getGUI().getServerInterface().drawCard(drawType);
                } catch (RemoteException e) {
                    System.err.println("Error communicating with the server: " + e.getMessage());
                }
            });
        } else {
            cardPane.setOnMouseClicked(mouseEvent -> {});
        }
    }
}
