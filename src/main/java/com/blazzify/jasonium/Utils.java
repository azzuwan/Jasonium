/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium;

import com.blazzify.jasonium.storage.Server;
import javafx.scene.control.Alert;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class Utils {

    public static void prompt(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean isNullorEmpty(String s) {
        return s.equals("") || s == null;
    }
    
}
