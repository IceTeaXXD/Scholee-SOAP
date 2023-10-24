/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.database;

import java.sql.*;
/**
 *
 * @author Matthew
 */
public class Database {
    protected Connection conn;
     /* USE DOTENV LATER */
    private static final String URL = "jdbc:mysql://localhost:3306/scholee_soap";
    private static final String user = "root";
    private static final String password = "root";
    
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
