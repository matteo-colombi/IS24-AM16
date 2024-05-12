package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private ListView<String> gamesList;
    @FXML
    private Button refreshButton;

    private ServerInterface serverInterface;
    private List<String> games;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        games = new ArrayList<>();
        gamesList.getItems().addAll(games);
    }

    public void refreshGamesList(ActionEvent event) {
        try {
            serverInterface.getGames();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setGamesList(List<String> games) {
        System.out.println(games);
        this.games = games;
        gamesList.getItems().clear();
        gamesList.getItems().addAll(games);
        gamesList.refresh();
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }
}
