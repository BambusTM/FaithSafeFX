module dev.yorun.faithsafe {
    requires javafx.controls;
    requires javafx.fxml;


    exports dev.yorun.faithsafe;
    opens dev.yorun.faithsafe to javafx.fxml;
}