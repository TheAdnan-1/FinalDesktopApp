module org.example.theadnan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.net.http;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;

    opens org.example.theadnan to javafx.fxml;
    exports org.example.theadnan;
}