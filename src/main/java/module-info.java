module com.example.brickbreakerminorproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.brickbreakerminorproject to javafx.fxml;
    exports com.example.brickbreakerminorproject;
}