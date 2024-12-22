module application.chatapp_dialog {
    requires javafx.controls;
    requires javafx.fxml;
//    requires javafx.web;
//
//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires net.synedra.validatorfx;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
//    requires com.almasb.fxgl.all;
//    requires annotations;
    requires java.sql;
    requires jdk.jdi;
//    requires com.fasterxml.jackson.annotation;
    requires jdk.xml.dom;
    requires jakarta.mail;
    requires org.postgresql.jdbc;
    requires java.desktop;
    opens application.chatapp_dialog.dto to javafx.base;
    opens application.chatapp_dialog.dal to javafx.base;
    opens application.chatapp_dialog to javafx.fxml;
    exports application.chatapp_dialog;
    exports application.chatapp_dialog.dal;
    exports application.chatapp_dialog.dto;
    exports application.chatapp_dialog.admin.modalcontroller;
    opens application.chatapp_dialog.admin.modalcontroller to javafx.fxml;
}