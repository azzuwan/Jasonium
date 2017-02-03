/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.models;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class Server{
    
   private String name;
   private String host;
   private String port;
   private String db;
   private String user;
   private String pass;
   private String type;

    public Server() {
    }

    public Server(String name, String host, String port, String user, String pass, String type) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.type = type;
    }
   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    /**
     * @return the db
     */
    public String getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(String db) {
        this.db = db;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
   @Override
    public String toString(){
        return this.name;
    }
            
}
