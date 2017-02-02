/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerTreeContextMenu extends ContextMenu {

    MenuItem connect = new MenuItem("Connect");
    MenuItem disconnect = new MenuItem("Disconnect");
    MenuItem remove = new MenuItem("Remove");

    public ServerTreeContextMenu() {
        connect.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Connecting!", ButtonType.OK);
            alert.showAndWait();
        });
        disconnect.setOnAction((event) -> {
        });
        remove.setOnAction((event) -> {
        });
        this.getItems().addAll(connect, disconnect, remove);
    }

}
