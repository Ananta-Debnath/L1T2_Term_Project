module com.example.l1t2_term_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.almasb.fxgl.all;

    opens com.example.l1t2_term_project.Controller to javafx.fxml;
    exports com.example.l1t2_term_project;
}