/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.database.Database;
import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
@WebService(endpointInterface="com.orden.soap.services.ScholarshipAcceptanceService")
public class ScholarshipAcceptanceServiceImpl implements ScholarshipAcceptanceService {
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
    public String setAcceptance(int uid, int uis, int sid, String status) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "INSERT INTO scholarship_acceptance VALUES (?,?,?,?)";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uid);
                stmt.setInt(2, uis);
                stmt.setInt(3, sid);
                stmt.setString(4, status);
                stmt.execute();

                if(stmt.getUpdateCount() > 0){
                    Logging log = new Logging("ACCEPTANCE SET", exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    return "Success";
                }else{
                    return "Fail";
                }
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Fail";
            }
        }else{
            return "Illegal Process";
        }
    }

    @Override
    public String getAcceptanceStatus(int uid, int uis, int sid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "SELECT status FROM scholarship_acceptance WHERE user_id_student = ? AND user_id_scholarship = ? AND scholarship_id = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uid);
                stmt.setInt(2, uis);
                stmt.setInt(3, sid);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Logging log = new Logging("ACCEPTANCE SET", exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    return rs.getString("status");
                }else{
                    return "Fail";
                }
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Fail";
            }
        }else{
            return "Illegal Process";
        }
    }
    
}
