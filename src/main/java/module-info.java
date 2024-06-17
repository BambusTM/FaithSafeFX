module dev.yorun.faithsafe {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
  requires passay;

  exports dev.yorun.faithsafe.service;
    exports dev.yorun.faithsafe;
    exports dev.yorun.faithsafe.algo;
    opens dev.yorun.faithsafe to javafx.fxml;
    opens dev.yorun.faithsafe.service to com.fasterxml.jackson.databind;
  exports dev.yorun.faithsafe.objects;
  opens dev.yorun.faithsafe.objects to com.fasterxml.jackson.databind;
}
