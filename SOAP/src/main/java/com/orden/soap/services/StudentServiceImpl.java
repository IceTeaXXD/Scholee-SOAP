/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.database.Database;
import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author henryanand
 */
@WebService(endpointInterface="com.orden.soap.services.StudentService")
public class StudentServiceImpl implements StudentService{
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
    public String registerStudent(int std_id_php) {
        if (validateAPIKey()) {
            String query = "INSERT INTO student (std_id_php) VALUES (?)";
            String returnVal;
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, std_id_php);

                stmt.execute();

                if (stmt.getUpdateCount() > 0) {
                    /* TODO: Add SOAP or REST Information on Description */
                    Logging log = new Logging("REGISTRATION ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    returnVal = "Register Success";
                } else {
                    returnVal = "Register Unsuccessful";
                }

                return returnVal;
            } catch (SQLException ex) {
                Logger.getLogger(StudentService.class.getName()).log(Level.SEVERE, null, ex);
                return "Failed";
            }
        } else {
            return "Illegal Process";
        }
    }
}