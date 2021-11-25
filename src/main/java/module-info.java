module com.example.iitgv10 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;

    opens com.example.iitgv10 to javafx.fxml;
    exports com.example.iitgv10;
    exports com.example.iitgv10.Controller;
    opens com.example.iitgv10.Controller to javafx.fxml;
}