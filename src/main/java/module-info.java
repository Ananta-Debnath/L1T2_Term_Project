module com.example.l1t2_term_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.almasb.fxgl.all;
    requires annotations;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;

    opens com.example.l1t2_term_project.Model.Player to javafx.base;
    opens com.example.l1t2_term_project.Model to javafx.base;
    opens com.example.l1t2_term_project.Controller to javafx.fxml;
    exports com.example.l1t2_term_project;
    exports com.example.l1t2_term_project.DTO;
    exports com.example.l1t2_term_project.Utils;
}