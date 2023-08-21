module com.example.chefideasapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires httpclient;
    requires httpcore;
    requires java.desktop;

    opens com.programmazione_avanzata.final_project.chef_ideas_app to javafx.fxml;
    exports com.programmazione_avanzata.final_project.chef_ideas_app;
    exports com.programmazione_avanzata.final_project.chef_ideas_app.controller;
    exports com.programmazione_avanzata.final_project.chef_ideas_app.dto to com.fasterxml.jackson.databind;
    opens com.programmazione_avanzata.final_project.chef_ideas_app.controller to javafx.fxml;
    opens com.programmazione_avanzata.final_project.chef_ideas_app.dto to javafx.base;
    exports com.programmazione_avanzata.final_project.chef_ideas_app.utilities;
    opens com.programmazione_avanzata.final_project.chef_ideas_app.utilities to javafx.fxml;
    exports com.programmazione_avanzata.final_project.chef_ideas_app.exception;
    opens com.programmazione_avanzata.final_project.chef_ideas_app.exception to javafx.fxml;
    exports com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup;
    opens com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup to javafx.fxml;
}