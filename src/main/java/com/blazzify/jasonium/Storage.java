/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class Storage {
    private static DB db = null;
    private static ConcurrentMap servers = null;
    private static Path path = null;    
    private Storage(){}
    public static ConcurrentMap getServers() {        
        path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "jasonium.db");
        System.out.println("Checking local storage at: " + path);
        if (Files.notExists(path) || db == null || db.isClosed()) {
            db = DBMaker.fileDB(path.toString()).make();            
            servers = db.hashMap("servers")                    
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)                    
                    .createOrOpen();
            System.out.println("Servers length: " + getServers().size());
        } else{          
            System.out.println("Strange cannot open map db");
        }
        //db.close();
        return servers;
    }
    
    public static void close(){
        db.close();
    }
    
    public static void save(ConcurrentMap modified){
       if (Files.notExists(path)) {
            db = DBMaker.fileDB(path.toString()).make();
            servers = db.hashMap("servers")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
            servers.putAll(modified);
            db.commit();
        } else {
            db = DBMaker.fileDB(path.toString()).make();
            servers = db.get("servers");
            servers.putAll(modified);
            db.commit();
            System.out.println("Servers length: " + getServers().size());

        }
        db.close();
    }
    
}
