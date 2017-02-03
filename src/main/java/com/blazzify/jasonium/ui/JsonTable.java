/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeTableView;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class JsonTable extends TreeTableView<ObjectNode>{
    ObjectMapper mapper = new ObjectMapper();
    public JsonTable() {
        try {
            JsonNode rootNode = mapper.readValue(new File("/home/azzuwan/Projects/JavaFX/Jasonium/data.json"),JsonNode.class);
            
        } catch (IOException ex) {
            Logger.getLogger(JsonTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
