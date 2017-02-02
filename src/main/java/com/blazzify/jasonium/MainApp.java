package com.blazzify.jasonium;

import com.blazzify.jasonium.models.Server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

public class MainApp extends Application {

    BorderPane root = new BorderPane();
    private Tab defaultTab = new Tab("Untitled*");
    private BorderPane defaultTabPane = new BorderPane();
    private TabPane tabPane = new TabPane();
    private TreeView<Server> serverTree = new TreeView<>();
    DB storage = null;
    ConcurrentMap servers = null;
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void start(Stage stage) throws Exception {
        //Check if local storage exists.
        checkExistingStorage();

        //Open database connection to H2
        //Vertical box to stack menu and tool bar
        VBox boxTop = new VBox();

        //The menubar
        MenuBar menuBar = new MenuBar();

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
            Dialog<Map<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Add New Connection");
            dialog.setHeaderText("Database Connection Details ");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField label = new TextField();
            label.setPromptText("Connection name");

            TextField host = new TextField();
            host.minWidth(400.0);
            host.setPromptText("Domain, host name or  IP Address");
            TextField port = new TextField();
            port.setPromptText("Port");

            TextField user = new TextField();
            user.setPromptText("Username");

            PasswordField pass = new PasswordField();
            pass.setPromptText("Password");

//            ChoiceBox type = new ChoiceBox();
//            type.getItems().add(new ServerType("H2 Embedded", "embedded"));
//            type.getItems().add(new ServerType("H2 Server", "h2"));
//            type.getItems().add(new ServerType("MongoDB", "mongodb"));
//            type.getItems().add(new ServerType("MariaDB", "mariadb"));
//            type.getItems().add(new ServerType("Mysql", "mysql"));
//            type.getItems().add(new ServerType("Postgresql", "postgresql"));

            grid.add(new Label("Label:"), 0, 0);
            grid.add(label, 1, 0);
            grid.add(new Label("Host:"), 0, 1);
            grid.add(host, 1, 1);
            grid.add(new Label("port:"), 0, 2);
            grid.add(port, 1, 2);
            grid.add(new Label("Username:"), 0, 3);
            grid.add(user, 1, 3);
            grid.add(new Label("Password:"), 0, 4);
            grid.add(pass, 1, 4);
//            grid.add(new Label("Server Type:"), 0, 5);
//            grid.add(type, 1, 5);

            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.getChildren().add(grid);
            dialog.getDialogPane().setContent(box);
            Platform.runLater(() -> label.requestFocus());
            dialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    Map<String, String> details = new HashMap<>();
                    details.put("name", label.getText());
                    details.put("host", host.getText());
                    details.put("port", port.getText());
                    details.put("user", user.getText());
                    details.put("pass", pass.getText());
//                    details.put("type", ((ServerType) type.getValue()).getValue());
                    return details;
                }
                return null;
            });
            Optional<Map<String, String>> result = dialog.showAndWait();
            System.out.println("Details gathered: " + result.get());
            addConnection(result);
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
        menuBar.getMenus().addAll(menuFile, menuFind, menuConnections, menuView, menuInfo);

        //Toolbar
        ToolBar toolBar = new ToolBar();

        Image btnNewImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/file-new.png"));
        Button btnNew = new Button("", new ImageView(btnNewImg));

        Image btnOpenImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/file-open.png"));
        Button btnOpen = new Button("", new ImageView(btnOpenImg));
        btnOpen.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                TableView table = buildTable(file);
                BorderPane tablePane = new BorderPane(table);
                tablePane.setStyle("-fx-padding: 0px 6px 6px 6px;");
                tablePane.setCenter(table);
                defaultTabPane.setCenter(tablePane);
                defaultTab.setText(file.getName());
            }

        });
        Image btnSaveImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/file-save.png"));
        Button btnSave = new Button("", new ImageView(btnSaveImg));
        Image btnSaveAsImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/file-save-as.png"));
        Button btnSaveAs = new Button("", new ImageView(btnSaveAsImg));
        TextField textSearch = new TextField();
        textSearch.setPrefWidth(300);
        textSearch.setPromptText("Search");
        toolBar.getItems().addAll(btnNew, btnOpen, btnSave, btnSaveAs, textSearch);

        //Vertical box add menu and tool bar
        boxTop.getChildren().add(menuBar);
        boxTop.getChildren().add(toolBar);

        //Main panel top
        root.setTop(boxTop);

        //Main panel left panel
        root.setLeft(buildServersTree());

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
        stage.show();
    }

    private TreeView buildServersTree() {

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
        serverTree.setRoot(rootNode);        
        return serverTree;
    }

    private TableView buildTable(File file) {
        TableView table = new TableView();

        table.setEditable(true);

        TableColumn indexCol = new TableColumn("#");
        indexCol.setCellValueFactory(new PropertyValueFactory<>("index"));
        indexCol.setMinWidth(25.0);
        indexCol.setMaxWidth(80.0);

        TableColumn intentCol = new TableColumn("Intent");
        intentCol.setCellValueFactory(new PropertyValueFactory<>("intent"));
        intentCol.setCellFactory(TextFieldTableCell.forTableColumn());
        intentCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Intent, String>>() {
            @Override
            public void handle(CellEditEvent<Intent, String> t) {
                ((Intent) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setIntent(t.getNewValue());
            }
        }
        );
        intentCol.setPrefWidth(300.0);

        TableColumn textCol = new TableColumn("Text");
        textCol.setCellValueFactory(new PropertyValueFactory<>("text"));
        textCol.setCellFactory(TextFieldTableCell.forTableColumn());
        textCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Intent, String>>() {
            @Override
            public void handle(CellEditEvent<Intent, String> t) {
                ((Intent) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setText(t.getNewValue());
            }
        }
        );
        textCol.setPrefWidth(400.0);

        TableColumn entitiesCol = new TableColumn("Entities");
        entitiesCol.setCellValueFactory(new PropertyValueFactory<>("entities"));
        entitiesCol.setCellFactory(TextFieldTableCell.forTableColumn());
        entitiesCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Intent, String>>() {
            @Override
            public void handle(CellEditEvent<Intent, String> t) {
                ((Intent) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setEntities(t.getNewValue());
            }
        }
        );
        entitiesCol.setPrefWidth(300.0);

        table.getColumns().addAll(indexCol, intentCol, textCol, entitiesCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<Intent> intentList = FXCollections.observableArrayList();

        table.setItems(intentList);
        table.setStyle("-fx-border-width: 1px;");
        table.setStyle("-fx-border-color: gray;");

        return table;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/jasonium", "sa", "");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Storage Error");
            alert.setHeaderText("Failed to read Jasonium database");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            System.exit(1);
        }
        return conn;
    }

    private void checkExistingStorage() {
        Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "jasonium.db");
        System.out.println("Checking local storage at: "+ path);
        if (Files.notExists(path)) {
            storage = DBMaker.fileDB(path.toString()).make();
            servers = storage.hashMap("servers")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();

        } else {
            storage = DBMaker.fileDB(path.toString()).make();
            servers = storage.get("servers");
            System.out.println("Servers length: " + servers.size());
        
        }
    }

    

    private void addConnection(Optional<Map<String, String>> details) {
        Map<String, String> detail = details.get();
        
        String serialized = null;
        try {
            serialized = mapper.writeValueAsString(detail);
            System.out.println("NEW CONNECTION SERIALIZED: " + serialized);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        servers.put(detail.get("name"), serialized);
        Server s = new Server(detail.get("name"),                 
                detail.get("host"), 
                detail.get("port"), 
                detail.get("user"),                 
                detail.get("pass"),
                ""
//                detail.get("type")
        ); 
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("icons/server-off.png"));
        ImageView iv = new ImageView(img);
       TreeItem<Server> node = new TreeItem<>(s,iv);
        serverTree.getRoot().getChildren().add(node);
        System.out.println("NEW CONNECTION INSERTED");
        

    }
    
    @Override
    public void stop(){
        System.out.println("Shutdown requested");
        System.out.println("Closing local storage...");
        storage.close();
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
