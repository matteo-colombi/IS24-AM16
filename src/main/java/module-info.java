module it.polimi.ingsw.am16 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires org.controlsfx.controls;
    requires java.rmi;
    requires java.desktop;
    requires jdk.dynalink;

    opens it.polimi.ingsw.am16 to javafx.fxml;
    opens it.polimi.ingsw.am16.common.model.cards to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.util to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.server.lobby to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.game to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.players to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.players.hand to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.cards.decks to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.model.chat to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.client.view.cli to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.tcpMessages to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.tcpMessages.response to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.common.tcpMessages.request to com.fasterxml.jackson.databind;
    opens it.polimi.ingsw.am16.server.controller to com.fasterxml.jackson.databind;
    exports it.polimi.ingsw.am16.server to java.rmi;
    exports it.polimi.ingsw.am16.server.rmi to java.rmi;
    exports it.polimi.ingsw.am16.server.lobby to java.rmi;
    exports it.polimi.ingsw.am16.server.controller to java.rmi;
    exports it.polimi.ingsw.am16.client to java.rmi;
    exports it.polimi.ingsw.am16.client.rmi to java.rmi;
    exports it.polimi.ingsw.am16.client.view to java.rmi;
    exports it.polimi.ingsw.am16.common.model.cards to java.rmi;
    exports it.polimi.ingsw.am16.common.model.cards.decks to java.rmi;
    exports it.polimi.ingsw.am16.common.model.players to java.rmi;
    exports it.polimi.ingsw.am16.common.model.players.hand to java.rmi;
    exports it.polimi.ingsw.am16.common.model.game to java.rmi;
    exports it.polimi.ingsw.am16.common.util to java.rmi;
    exports it.polimi.ingsw.am16.common.exceptions to java.rmi;
    exports it.polimi.ingsw.am16.common.model.chat to java.rmi;

    opens it.polimi.ingsw.am16.client.view.gui;
    opens it.polimi.ingsw.am16.client.view.gui.controllers;
    opens it.polimi.ingsw.am16.client.view.gui.util;
}