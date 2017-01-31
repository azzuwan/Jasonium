package com.blazzify.jasonium;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javafx.util.Pair;
import org.apache.metamodel.UpdateCallback;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.schema.ColumnType;
import org.apache.metamodel.schema.Schema;
import org.apache.metamodel.schema.Table;

public class MainApp extends Application {

    private Tab defaultTab = new Tab("Untitled*");
    private BorderPane defaultTabPane = new BorderPane();
    private Connection h2Connection = null;

    @Override
    public void start(Stage stage) throws Exception {
        h2Connection = connect();
        //Main panel
        BorderPane root = new BorderPane();
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
        menuItemAddConnection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Add New Connection");
                dialog.setHeaderText("Database Connection Details ");
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));
                //grid.setPrefWidth(400.0);
                
                TextField host = new TextField();
                host.minWidth(400.0);
                host.setPromptText("Domain / host name / IP Address");
                TextField port = new TextField();
                port.setPromptText("Port");
                
                TextField user = new TextField();
                user.setPromptText("Username");
                
                PasswordField pass = new PasswordField();
                pass.setPromptText("Password");
                
                grid.add(new Label("Host:"), 0,0);
                grid.add(host, 1, 0);
                grid.add(new Label("port:"), 0,1);
                grid.add(port, 1, 1);
                grid.add(new Label("Username:"), 0,2);
                grid.add(user, 1, 2);
                grid.add(new Label("Password:"), 0,3);
                grid.add(pass, 1, 3);
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER);
                box.getChildren().add(grid);
                dialog.getDialogPane().setContent(box);
                Optional<Pair<String, String>> result = dialog.showAndWait();

                //createConnection("localhost", "", "", "");
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
        root.setLeft(buildConnectionTreeView());

        //Tool bar for tab content
        ToolBar defaultTabTool = new ToolBar();

        //Set tab tool bar to panel
        defaultTabPane.setTop(defaultTabTool);
        //The first default tab

        //Add the default panel to default tab
        defaultTab.setContent(defaultTabPane);

        //Tab panel container
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(defaultTab);

        root.setCenter(tabPane);
        Scene scene = new Scene(root, 1024, 768);

        stage.setTitle("Jasonium");
        stage.setScene(scene);
        stage.show();
    }

    private TreeView buildConnectionTreeView() {
        TreeView<String> tree = new TreeView<>();
        Image imgRoot = new Image(getClass().getClassLoader().getResourceAsStream("icons/servers.png"));
        TreeItem root = new TreeItem("Servers", new ImageView(imgRoot));
        tree.setRoot(root);
        Image imgLocal = new Image(getClass().getClassLoader().getResourceAsStream("icons/server-off.png"));
        TreeItem localhost = new TreeItem("Local MongoDB", new ImageView(imgLocal));
        root.getChildren().add(localhost);
        return tree;
    }

    private TableView buildTable(File file) {
        TableView table = new TableView();
        ReadContext ctx = null;
        try {
            ctx = JsonPath.parse(file);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Map<String, LinkedHashMap>> entityList = ctx.read("$.rasa_nlu_data.entity_examples");
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
        int i = 0;
        for (Object entity : entityList) {
            LinkedHashMap map = (LinkedHashMap) entity;
            Set<String> keys = map.keySet();
            Intent intent = new Intent(Integer.toString(i), map.get("intent").toString(),
                    map.get("text").toString(),
                    map.get("entities").toString());
            intentList.add(intent);
            i++;
        }
        table.setItems(intentList);
        table.setStyle("-fx-border-width: 1px;");
        table.setStyle("-fx-border-color: gray;");

        return table;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/store", "sa", "");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private void createConnection(String host, String port, String user, String password) {
        JdbcDataContext h2 = new JdbcDataContext(this.h2Connection);
        Schema schema = h2.getSchemaByName("PUBLIC");
        if (schema != null) {
            Table table = schema.getTableByName("CONNECTIONS");
            if (table != null) {
                System.out.println("Table exist");
            } else {
                createConnectionTable();
            }
        } else {
            System.out.println("No such schema in create connection");
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void createConnectionTable() {
        JdbcDataContext h2 = new JdbcDataContext(h2Connection);
        h2.executeUpdate((UpdateCallback uc) -> {
            Schema[] list = h2.getSchemas();
            for (Schema s : list) {
                System.out.println("Schema => " + s.getName());
                System.out.println("Default Schema: " + h2.getDefaultSchema().getName());
            }
            Schema schema = h2.getSchemaByName("PUBLIC");
            if (schema != null) {
                uc.createTable("PUBLIC", "CONNECTIONS")
                        .withColumn("id").ofType(ColumnType.INTEGER)
                        .withColumn("name").ofType(ColumnType.VARCHAR)
                        .withColumn("host").ofType(ColumnType.VARCHAR)
                        .withColumn("port").ofType(ColumnType.INTEGER)
                        .withColumn("user").ofType(ColumnType.VARCHAR)
                        .withColumn("password").ofType(ColumnType.VARCHAR)
                        .execute();
                System.out.println("Table created");
            } else {
                System.out.println("No such schema in create connection table");
            }

        });
    }

}
