/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;
import com.orden.soap.database.Database;
import com.sun.net.httpserver.HttpExchange;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;

/**
 *
 * @author henryanand
 */
public abstract class BaseService {
    protected static final Database db = new Database();

    protected Dotenv dotenv = Dotenv.load();

    protected String service;

    @Resource
    protected WebServiceContext wsContext;

    protected boolean validateAPIKey() {
        try {
            MessageContext mc = wsContext.getMessageContext();

            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");

            /* Check for the API KEYS*/
            String query = "SELECT service_name FROM apikeys WHERE key_value = ?";

            PreparedStatement stmt = db.getConnection().prepareStatement(query);

            stmt.setString(1, apiKey);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                this.service = rs.getString("service_name");
                System.out.println("X-API-KEY: " + apiKey);
                return true;
            }else{
                System.out.println("Invalid X-API-KEY: " + apiKey);
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    protected String getSource() {
        return this.service;
    }
}
