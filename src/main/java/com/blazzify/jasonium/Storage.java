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
    private static DB storage = null;
    private static ConcurrentMap servers = null;
    private static Path path = null;
    public static ConcurrentMap getServers() {        
        path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "jasonium.db");
        System.out.println("Checking local storage at: " + path);
        if (Files.notExists(path)) {
            storage = DBMaker.fileDB(path.toString()).make();
            servers = storage.hashMap("servers")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
        } else {
            storage = DBMaker.fileDB(path.toString()).make();
            servers = storage.get("servers");
            System.out.println("Servers length: " + getServers().size());

        }
        storage.close();
        return servers;
    }
    
    public static void close(){
        storage.close();
    }
    
    public static void save(ConcurrentMap modified){
       if (Files.notExists(path)) {
            storage = DBMaker.fileDB(path.toString()).make();
            servers = storage.hashMap("servers")
                    .keySerializer(Serializer.STRING)
                    .valueSerializer(Serializer.STRING)
                    .create();
            servers.putAll(modified);
            storage.commit();
        } else {
            storage = DBMaker.fileDB(path.toString()).make();
            servers = storage.get("servers");
            servers.putAll(modified);
            storage.commit();
            System.out.println("Servers length: " + getServers().size());

        }
        storage.close();
    }
    
}
