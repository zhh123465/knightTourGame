module com.knighttour {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    
    exports com.knighttour;
    exports com.knighttour.controller;
    exports com.knighttour.model;
    exports com.knighttour.view;
    exports com.knighttour.algorithm;
    exports com.knighttour.util;
}
