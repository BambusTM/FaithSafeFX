module dev.yorun.faithsafe {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    exports dev.yorun.faithsafe.service;
    exports dev.yorun.faithsafe;
    opens dev.yorun.faithsafe to javafx.fxml;
}