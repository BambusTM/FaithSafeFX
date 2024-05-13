module dev.yorun.faithsafe {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.yorun.faithsafe to javafx.fxml;
    exports dev.yorun.faithsafe;
}