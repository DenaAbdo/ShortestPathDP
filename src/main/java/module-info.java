module com.example.algodpbetweencitiesproject12024 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.algodpbetweencitiesproject12024 to javafx.fxml;
    exports com.example.algodpbetweencitiesproject12024;
}