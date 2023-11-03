/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.database;

import java.sql.*;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {
    protected Connection conn;
    Dotenv dotenv = Dotenv.load();
    private String URL = dotenv.get("DB_URL");
    private String user = dotenv.get("DB_USER");
    private String password = dotenv.get("DB_PASSWORD");
    
    public Database(){
        try {
            this.conn = DriverManager.getConnection(URL, user, password);
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
    }
    
    public Connection getConnection(){
        return this.conn;
    }
}
