module org.buluton {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.buluton to javafx.fxml;
    exports org.buluton;
}