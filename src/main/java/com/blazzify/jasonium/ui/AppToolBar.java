/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.blazzify.jasonium.Intent;
import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class AppToolBar extends ToolBar {

    public AppToolBar(Stage stage, Tab defaultTab, BorderPane defaultTabPane) {
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
        this.getItems().addAll(btnNew, btnOpen, btnSave, btnSaveAs, textSearch);

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
                new EventHandler<TableColumn.CellEditEvent<Intent, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Intent, String> t) {
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
                new EventHandler<TableColumn.CellEditEvent<Intent, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Intent, String> t) {
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
                new EventHandler<TableColumn.CellEditEvent<Intent, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Intent, String> t) {
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

    
}
