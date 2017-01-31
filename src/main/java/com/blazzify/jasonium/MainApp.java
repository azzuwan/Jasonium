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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
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
        menuBar.getMenus().addAll(menuFile, menuFind, menuView, menuInfo);

        //Toolbar
        ToolBar toolBar = new ToolBar();

        Image btnNewImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/doc-new.png"));
        Button btnNew = new Button("", new ImageView(btnNewImg));

        Image btnOpenImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/doc-open.png"));
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
        Image btnSaveImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/doc-save.png"));
        Button btnSave = new Button("", new ImageView(btnSaveImg));
        Image btnSaveAsImg = new Image(getClass().getClassLoader().getResourceAsStream("icons/doc-save-as.png"));
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
    private Tab defaultTab = new Tab("Untitled*");
    private BorderPane defaultTabPane = new BorderPane();

    private TreeView buildTreeView(File file) {
        TreeView<String> tree = new TreeView<>();
        try {
            ReadContext ctx = JsonPath.parse(file);
            List<Map<String, LinkedHashMap>> entityList = ctx.read("$.rasa_nlu_data.entity_examples");
            TreeItem<String> treeRoot = new TreeItem<>("Root");
            treeRoot.setExpanded(true);
            int i = 0;
            System.out.println("ENTITY LIST TYPE: " + entityList.getClass());
            for (Object entity : entityList) {
                System.out.println("ENTITY CLASS: " + entity.getClass());
                TreeItem<String> item = new TreeItem<>(Integer.toString(i));
                LinkedHashMap map = (LinkedHashMap) entity;
                Set<String> keys = map.keySet();
                for (String k : keys) {
                    System.out.println("OBJECT K: " + k.getClass());
                    String v = map.get(k).toString();
                    TreeItem<String> p = new TreeItem<>(v);
                    item.getChildren().add(p);
                }
                i++;
                treeRoot.getChildren().add(item);

            }
            tree.setRoot(treeRoot);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
