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
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import static javafx.scene.input.KeyCode.T;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerTreeItem extends TreeItem implements EventTarget, EventDispatchChain{
   
    private Server s;
    public ServerTreeItem() {
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            System.out.println("HEHEHE" + event.getSource());
        });
        
    }
    
    public ServerTreeItem(Server t, Node graphic ){
        super(t, graphic);
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            System.out.println("HEHEHE" + event.getSource());
        });
        
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

    @Override
    public EventDispatchChain append(EventDispatcher eventDispatcher) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EventDispatchChain prepend(EventDispatcher eventDispatcher) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Event dispatchEvent(Event event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
