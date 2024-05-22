package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.common.model.cards.Cornerable;
import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class InfoTableController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoTableTexts = new HashMap<>();
        infoTableTexts.put(ResourceType.FUNGI, fungiAmount);
        infoTableTexts.put(ResourceType.PLANT, plantAmount);
        infoTableTexts.put(ResourceType.ANIMAL, animalAmount);
        infoTableTexts.put(ResourceType.INSECT, insectAmount);
        infoTableTexts.put(ObjectType.INKWELL, inkwellAmount);
        infoTableTexts.put(ObjectType.MANUSCRIPT, manuscriptAmount);
        infoTableTexts.put(ObjectType.QUILL, quillAmount);
    }

    public void updateInfoTable(Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
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

    public Parent getRoot() {
        return root;
    }
}
