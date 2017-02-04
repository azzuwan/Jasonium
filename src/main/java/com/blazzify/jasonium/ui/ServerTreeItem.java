/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.ui;

import com.blazzify.jasonium.models.Server;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerTreeItem extends TreeItem{
   
    private Server s;
    public ServerTreeItem() {
        super();
        
    }
    
    public ServerTreeItem(Server t, Node graphic ){
        super(t, graphic);
        
    }
    
     /**
     * @return the t
     */
    public Server getServer() {
        return s;
    }

    /**
     * @param t the t to set
     */
    public void setT(Server server) {
        this.s = s;
    } 
}
