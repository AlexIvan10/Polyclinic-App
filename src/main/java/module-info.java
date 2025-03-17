module org.example.test {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens org.example.test to javafx.fxml;
    exports org.example.test;
}