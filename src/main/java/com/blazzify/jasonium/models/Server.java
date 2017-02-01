/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazzify.jasonium.models;

import org.javalite.activejdbc.Model;

/**
 *
 * @author Azzuwan Aziz <azzuwan@gmail.com>
 */
public class Server extends Model{
    @Override
    public String toString(){
        return this.getString("name");
    }
}
