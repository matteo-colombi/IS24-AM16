module it.polimi.ingsw.am16 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires org.controlsfx.controls;

    opens it.polimi.ingsw.am16 to javafx.fxml;
    exports it.polimi.ingsw.am16;
}