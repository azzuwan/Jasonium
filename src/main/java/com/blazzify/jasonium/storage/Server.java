/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.storage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
@Entity
@Table(name = "SERVERS", catalog = "JASONIUM", schema = "PUBLIC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Server.findAll", query = "SELECT s FROM Server s")
    , @NamedQuery(name = "Server.findById", query = "SELECT s FROM Server s WHERE s.id = :id")
    , @NamedQuery(name = "Server.findByName", query = "SELECT s FROM Server s WHERE s.name = :name")
    , @NamedQuery(name = "Server.findByHost", query = "SELECT s FROM Server s WHERE s.host = :host")
    , @NamedQuery(name = "Server.findByPort", query = "SELECT s FROM Server s WHERE s.port = :port")
    , @NamedQuery(name = "Server.findByDb", query = "SELECT s FROM Server s WHERE s.db = :db")
    , @NamedQuery(name = "Server.findByUser", query = "SELECT s FROM Server s WHERE s.user = :user")
    , @NamedQuery(name = "Server.findByPass", query = "SELECT s FROM Server s WHERE s.pass = :pass")
    , @NamedQuery(name = "Server.findByType", query = "SELECT s FROM Server s WHERE s.type = :type")})
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String name;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String host;
    @Basic(optional = false)
    @Column(nullable = false)
    private int port;
    @Basic(optional = false)
    @Column(nullable = false, length = 255)
    private String db;
    @Column(length = 255)
    private String user;
    @Column(length = 255)
    private String pass;
    @Column(length = 20)
    private String type;

    public Server() {
    }

    public Server(Long id) {
        this.id = id;
    }

    public Server(String name, String host, int port, String db) {        
        this.name = name;
        this.host = host;
        this.port = port;
        this.db = db;
    }
    
    public Server(Long id, String name, String host, int port, String db) {
        this.id = id;
        this.name = name;
        this.host = host;
        this.port = port;
        this.db = db;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Server)) {
            return false;
        }
        Server other = (Server) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        //return "com.blazzify.jasonium.storage.Server[ id=" + id + " ]";
        return this.getName();
    }
    
}
