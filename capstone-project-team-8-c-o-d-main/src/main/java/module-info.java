module org.team8.capstoneprojectteam8cod2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires junit;
    requires org.testng;

    // Open FXML packages to JavaFX
    opens org.team8.capstoneprojectteam8cod2 to javafx.fxml;
    opens org.team8.controller to javafx.fxml;

    // Open client package to BOTH Jackson (for JSON) and JavaFX (for PropertyValueFactory)
    opens org.team8.client to com.fasterxml.jackson.databind, javafx.base;

    // Export your main package so Launcher or JavaFX can start the app
    exports org.team8.capstoneprojectteam8cod2;
}
