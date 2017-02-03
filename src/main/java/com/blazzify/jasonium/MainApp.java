package com.blazzify.jasonium;

import com.blazzify.jasonium.ui.AppMenu;
import com.blazzify.jasonium.ui.AppToolBar;
import com.blazzify.jasonium.ui.ServerTree;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class MainApp extends Application {
    BorderPane root = new BorderPane();
    private Tab defaultTab = new Tab("Untitled*");
    private BorderPane defaultTabPane = new BorderPane();
    private TabPane tabPane = new TabPane();
    private ServerTree serverTree;
    private ConcurrentMap servers = null;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void start(Stage stage) throws Exception {
        
        //Tool bar for tab content
        ToolBar defaultTabTool = new ToolBar();

        //Set tab tool bar to panel
        defaultTabPane.setTop(defaultTabTool);
        //The first default tab

        //Add the default panel to default tab
        defaultTab.setContent(defaultTabPane);

        //Tab panel container
        tabPane.getTabs().add(defaultTab);

        //root.setCenter(tabPane);
        Scene scene = new Scene(root, 1024, 768);

        stage.setTitle("Jasonium");
        stage.setScene(scene);
        //Calling show first because we need the window object 
        //to be passed to the custom ui components
        stage.show();

        //Main panel left panel
        serverTree = new ServerTree(stage.getScene().getWindow());
        root.setLeft(serverTree);

        AppMenu menuBar = new AppMenu(stage,serverTree);
        AppToolBar toolBar = new AppToolBar(stage, defaultTab, defaultTabPane);
        
        //Vertical box to stack menu and tool bar
        VBox boxTop = new VBox();
        boxTop.getChildren().add(menuBar);
        boxTop.getChildren().add(toolBar);
        
        //Main panel top
        root.setTop(boxTop);
    }

   

    @Override
    public void stop() {
        System.out.println("Shutdown requested");
        System.out.println("Closing local storage...");        
        System.out.println("Completed");
        Platform.exit();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
