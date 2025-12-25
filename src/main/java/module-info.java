module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.example.demo.model.piece;
    opens com.example.demo.model.piece to javafx.fxml;
    exports com.example.demo.model;
    opens com.example.demo.model to javafx.fxml;
    exports com.example.demo.logic;
    opens com.example.demo.logic to javafx.fxml;
    exports com.example.demo.ui;
    opens com.example.demo.ui to javafx.fxml;
}