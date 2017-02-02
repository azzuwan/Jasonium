/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.blazzify.jasonium.MainApp;
import com.blazzify.jasonium.models.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerTree extends TreeView<Server> {

    ObjectMapper mapper = new ObjectMapper();

    public ServerTree() {
    }
    
    public ServerTree(Window root, Map<String, String> servers) {
        Image imgRoot = new Image(getClass().getClassLoader().getResourceAsStream("icons/servers.png"));
        Server node = new Server();
        node.setName("Servers");
        TreeItem<Server> rootNode = new TreeItem<>(node, new ImageView(imgRoot));
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
            ServerTreeContextMenu menu = new ServerTreeContextMenu();
            Window w = root.getScene().getWindow();
            Double winX = w.getX();
            Double winY = w.getY();            
            menu.show(w, event.getSceneX() + winX, event.getSceneY() + winY);
        });
    }

}
