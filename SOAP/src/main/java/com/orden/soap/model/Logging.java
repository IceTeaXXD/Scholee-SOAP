/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;

import com.orden.soap.database.Database;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthew
 */
public class Logging extends Database{
    private String endpoint;
    private String description;
    private String ip_address;

    public Logging(String endpoint, String description, String ip_address) {
        super();
        this.endpoint = endpoint;
        this.description = description;
        this.ip_address = ip_address;
    }
    
    public void insertLogging(){
        try {
            String query = "INSERT INTO logging (endpoint, description, ip_address) VALUES (?,?,?)";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, this.endpoint);
            stmt.setString(2, this.description);
            stmt.setString(3, this.ip_address);
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}