/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;

import com.orden.soap.database.Database;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthew
 */
public class Logging extends Database{
    private String description;
    private String ip_address;

    public Logging(String description, String ip_address) {
        super();
        this.description = description;
        this.ip_address = ip_address;
    }
    
    public void insertLogging(){
        try {
            Statement stmt = this.conn.createStatement();
            String query = "INSERT INTO logging (description, ip_address)"
                    + " VALUES (" + this.description + "," + this.ip_address + ")";
            
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}