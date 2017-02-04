/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.blazzify.jasonium.storage.Server;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerTreeContextMenu extends ContextMenu {

    DB db = null;
    MenuItem connect = new MenuItem("Connect");
    MenuItem disconnect = new MenuItem("Disconnect");
    MenuItem remove = new MenuItem("Remove");

    public ServerTreeContextMenu(Server s) {
        connect.setOnAction((event) -> {
            System.out.println("Connection Info: " + s);
            System.out.println("OBJ: " + ToStringBuilder.reflectionToString(s));
            MongoCredential cred = MongoCredential.createCredential(s.getUser(), "admin", s.getPass().toCharArray());
            MongoClient client = new MongoClient(new ServerAddress(s.getHost(), s.getPort()));
            db = client.getDB(s.getDb());
        });
        disconnect.setOnAction((event) -> {
        });
        remove.setOnAction((event) -> {
        });
        this.getItems().addAll(connect, disconnect, remove);
    }

}
