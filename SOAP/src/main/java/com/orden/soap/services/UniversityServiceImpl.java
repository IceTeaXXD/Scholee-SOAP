/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.database.Database;
import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Matthew
 */
@WebService(endpointInterface="com.orden.soap.services.UniversityService")
public class UniversityServiceImpl implements UniversityService {
    
    private static final Database db = new Database();
    
    @Resource
    WebServiceContext wsContext;
    
    public Boolean validateAPIKey() {
        try {
            MessageContext mc = wsContext.getMessageContext();
            
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            String apiKey = exchange.getRequestHeaders().getFirst("API-KEY");
            
            if (apiKey != null && apiKey.equals("shortT_Key")) {
                System.out.println("API-KEY: " + apiKey);
                return true;
            } else {
                System.out.println("Invalid API-KEY: " + apiKey);
                return false;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle exceptions as needed
        }
    }
    
    @Override
    @WebMethod
    public String createUniversity(int rest_uni_id) {
        MessageContext mc = wsContext.getMessageContext();
            
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        
        try {
            String query = "INSERT INTO university (rest_uni_id) VALUES (?)";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1, rest_uni_id);
            if(stmt.execute()){
                /* Log it and Return Success */
                Logging log = new Logging("REST UNIVERSITY ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                return "Success";
            }else{
                return "Failed";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UniversityServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed";
        }
    }

    @Override
    @WebMethod
    public String setPHPId(int php_uni_id) {
        MessageContext mc = wsContext.getMessageContext();
            
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        
        try {
            String query = "INSERT INTO university (php_uni_id) VALUES (?)";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1, php_uni_id);
            if(stmt.execute()){
                /* Log it and Return Success */
                Logging log = new Logging("PHP UNIVERSITY ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                return "Success";
            }else{
                return "Failed";
            }
        } catch (SQLException ex) {
            Logger.getLogger(UniversityServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return "Failed";
        }
    }
    
}
