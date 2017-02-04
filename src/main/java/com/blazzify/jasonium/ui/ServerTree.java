/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.blazzify.jasonium.MainApp;
import com.blazzify.jasonium.Storage;
import com.blazzify.jasonium.models.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import javafx.util.Callback;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerTree extends TreeView<Server> {

    private ObjectMapper mapper = new ObjectMapper();

    public ServerTree() {
    }

    public ServerTree(Window root) {
        ConcurrentMap servers = Storage.getServers();
        //Storage.close();
        Image imgRoot = new Image(getClass().getClassLoader().getResourceAsStream("icons/servers.png"));
        Server node = new Server();
        node.setName("Servers");
        TreeItem<Server> rootNode = new TreeItem<Server>(node, new ImageView(imgRoot));
        servers.forEach((t, u) -> {
            Server s = new Server();
            try {
                s = mapper.readValue(u.toString(), Server.class);
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
            Image imgOffline = new Image(getClass().getClassLoader().getResourceAsStream("icons/server-off.png"));
            TreeItem<Server> child = new TreeItem<>(s, new ImageView(imgOffline));
            rootNode.getChildren().add(child);
        });
        this.setRoot(rootNode);

        this.setOnContextMenuRequested((event) -> {
           TreeItem<Server>  item = (TreeItem<Server>) this.getSelectionModel().getSelectedItem();
            Window w = root.getScene().getWindow();
            Double winX = w.getX();
            Double winY = w.getY();            
            System.out.println("TREE NODE RIGHT CLICK: "+ item);
            ServerTreeContextMenu menu = new ServerTreeContextMenu(item.getValue());
            menu.show(w, event.getSceneX() + winX, event.getSceneY() + winY);
        });
    }

}
