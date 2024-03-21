module it.polimi.ingsw.am16 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.am16 to javafx.fxml;
    opens it.polimi.ingsw.am16.common.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.util to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.lobby to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.game to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.players to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.players.hand to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.cards.decks to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.am16;
}