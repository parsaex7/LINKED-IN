module org.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires java.sql;
    requires GNAvatarView;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    exports org.example.model to com.fasterxml.jackson.databind;

    opens org.example.client to javafx.fxml;
    exports org.example.client;
}