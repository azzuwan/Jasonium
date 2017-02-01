/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class ServerType {
    String label;
    String value;

    public ServerType() {
    }

    public ServerType(String label, String value) {
        this.label = label;
        this.value = value;
    }
    
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString(){
        return this.label;
    }
}
