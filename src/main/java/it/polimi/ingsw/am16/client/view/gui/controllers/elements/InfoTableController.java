package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.common.model.cards.Cornerable;
import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller class for the info table GUI element. Used to display the amount of each resource and object currently visible on the player's play area.
 */
public class InfoTableController {

    @FXML
    private VBox root;

    @FXML
    private Text fungiAmount;
    @FXML
    private Text plantAmount;
    @FXML
    private Text animalAmount;
    @FXML
    private Text insectAmount;
    @FXML
    private Text inkwellAmount;
    @FXML
    private Text manuscriptAmount;
    @FXML
    private Text quillAmount;

    private Map<Cornerable, Text> infoTableTexts;

    private Map<ResourceType, Integer> resourceCounts;

    /**
     * Initializes the element, preparing it for later use.
     */
    @FXML
    public void initialize() {
        infoTableTexts = new HashMap<>();
        infoTableTexts.put(ResourceType.FUNGI, fungiAmount);
        infoTableTexts.put(ResourceType.PLANT, plantAmount);
        infoTableTexts.put(ResourceType.ANIMAL, animalAmount);
        infoTableTexts.put(ResourceType.INSECT, insectAmount);
        infoTableTexts.put(ObjectType.INKWELL, inkwellAmount);
        infoTableTexts.put(ObjectType.MANUSCRIPT, manuscriptAmount);
        infoTableTexts.put(ObjectType.QUILL, quillAmount);
        resourceCounts = new EnumMap<>(ResourceType.class);
    }

    /**
     * Updates the values displayed on the info table.
     * @param resourceCounts The amount of each resource currently visible on the player's play area.
     * @param objectCounts The amount of each object currently visible on the player's play area.
     */
    public void updateInfoTable(Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.resourceCounts = resourceCounts;
        for(Map.Entry<ResourceType, Integer> entry : resourceCounts.entrySet()) {
            ResourceType type = entry.getKey();
            Integer count = entry.getValue();
            Text text = infoTableTexts.get(type);
            text.setText(String.format("%02d", count));
        }
        for(Map.Entry<ObjectType, Integer> entry : objectCounts.entrySet()) {
            ObjectType type = entry.getKey();
            Integer count = entry.getValue();
            Text text = infoTableTexts.get(type);
            text.setText(String.format("%02d", count));
        }
    }

    /**
     * @return A map containing the amount of each resource currently visible on the player's play field.
     */
    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    /**
     * @return The root node of this info table element.
     */
    public Parent getRoot() {
        return root;
    }
}
