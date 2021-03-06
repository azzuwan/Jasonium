/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.blazzify.jasonium.Storage;
import com.blazzify.jasonium.Utils;
import com.blazzify.jasonium.storage.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class AppMenu extends MenuBar {

    ObjectMapper mapper = null;
    Map<String, String> servers = null;
    ServerTree serverTree = null;

    public AppMenu(Stage stage, ServerTree serverTree) {
        servers = Storage.getServers(); 
        //Storage.close();
        mapper = new ObjectMapper();
        this.serverTree = serverTree;
       
        //Top level File
        Menu menuFile = new Menu("File");
        //File -> Save        
        MenuItem menuItemSave = new MenuItem("Save");
        //File -> New
        MenuItem menuItemNew = new MenuItem("New");
        //File -> Open
        MenuItem menuItemOpen = new MenuItem("Open");
        menuItemOpen.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(stage);

        });
        menuFile.getItems().addAll(menuItemNew, menuItemOpen, menuItemSave);

        //Top level Connections
        Menu menuConnections = new Menu("Connections");
        MenuItem menuItemAddConnection = new MenuItem("Add connection");
        menuItemAddConnection.setOnAction((ActionEvent event) -> {
            Dialog<Server> dialog = new Dialog<>();
            dialog.setTitle("Add New Connection");
            dialog.setHeaderText("Database Connection Details ");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField label = new TextField();
            label.setPromptText("Name this connection");

            TextField host = new TextField();
            host.minWidth(400.0);
            host.setPromptText("Host name or IP Address");

            TextField port = new TextField();
            port.setPromptText("Defaults to 27017");

            TextField db = new TextField();
            db.setPromptText("Will create if none");

            TextField user = new TextField();
            user.setPromptText("Database username");

            PasswordField pass = new PasswordField();
            pass.setPromptText("Database password");

            grid.add(new Label("Label:"), 0, 0);
            grid.add(label, 1, 0);
            grid.add(new Label("Host:"), 0, 1);
            grid.add(host, 1, 1);
            grid.add(new Label("Database:"), 0, 2);
            grid.add(db, 1, 2);
            grid.add(new Label("port:"), 0, 3);
            grid.add(port, 1, 3);
            grid.add(new Label("Username:"), 0, 4);
            grid.add(user, 1, 4);
            grid.add(new Label("Password:"), 0, 5);
            grid.add(pass, 1, 5);

            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.getChildren().add(grid);
            dialog.getDialogPane().setContent(box);
            Platform.runLater(() -> label.requestFocus());
            dialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    Server server = new Server();
                    server.setName(label.getText());
                    
                    if (host.getText().equals("") || host.getText() == null) {
                        server.setHost("localhost");
                        
                    } else {
                        server.setHost(host.getText());
                    }
                    
                    if (port.getText().equals("") || port.getText() == null) {
                        server.setPort(27017);
                        
                    } else {
                        server.setPort(Integer.parseInt(port.getText()));
                    }
                    server.setDb(db.getText());
                    server.setUser(user.getText());
                    server.setPass(pass.getText());
                    return server;
                }
                return null;
            });
            Optional<Server> result = dialog.showAndWait();
            if (result != null && result.get()!= null) {
                System.out.println("Details gathered: " + result.get());
                Server s  = result.get();
                if(Utils.isNullorEmpty(s.getName())){
                    Utils.prompt(Alert.AlertType.ERROR, "Incomplete Detail", "Missing label or server name", "Please fill in server name or Label");
                }else if(Utils.isNullorEmpty(s.getHost())){
                    Utils.prompt(Alert.AlertType.ERROR, "Incomplete Detail", "Missing host name or IP Address", "Please fill in host name or IP Address");
                }else{
                    addConnection(result.get());
                }
            } else {
                System.out.println("Dialog canceled");
            }

        });
        menuConnections.getItems().add(menuItemAddConnection);
        //Top level Find
        Menu menuFind = new Menu("Find");

        //Top level  View
        Menu menuView = new Menu("View");

        //Top level About
        Menu menuInfo = new Menu("Info");
        MenuItem menuItemAbout = new MenuItem("About");
        menuItemAbout.setOnAction((event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Jasonium");
            alert.setHeaderText("Jasonium");
            alert.setContentText("Copyright © 2017 Azzuwan Aziz ");
            alert.showAndWait();
        });
        menuInfo.getItems().add(menuItemAbout);
        //Add all the top level menu
        this.getMenus().addAll(menuFile, menuFind, menuConnections, menuView, menuInfo);

    }



    private void addConnection(Server details) {
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("icons/server-off.png"));
        ImageView iv = new ImageView(img);
        TreeItem<Server> node = new TreeItem<>(details, iv);
        serverTree.getRoot().getChildren().add(node);
        System.out.println("NEW CONNECTION INSERTED");

    }

}
