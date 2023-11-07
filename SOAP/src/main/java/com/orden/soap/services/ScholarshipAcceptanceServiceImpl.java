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
    public String registerScholarshipApplication(int uid, int uis, int sid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                /* Find the REST Scholarship_ID */
                String query = "SELECT scholarship_id_rest FROM scholarship WHERE user_id_scholarship_php = ? AND scholarship_id_php = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis);
                stmt.setInt(2, sid);
                ResultSet rs = stmt.executeQuery();
                
                if(rs.next() && rs.getInt("scholarship_id_rest") != -1){
                    query = "INSERT INTO scholarship_acceptance VALUES (?,?,?,?,?)";
                    stmt = db.getConnection().prepareStatement(query);
                    stmt.setInt(1, uid);
                    stmt.setInt(2, uis);
                    stmt.setInt(3, sid);
                    stmt.setInt(4, rs.getInt("scholarship_id_rest"));
                    stmt.setString(5, "waiting");
                    stmt.execute();

                    if(stmt.getUpdateCount() > 0){
                        Logging log = new Logging("REGISTERED WITH STATUS WAITING", exchange.getRemoteAddress().getAddress().getHostAddress());
                        log.insertLogging();
                        return "Success";
                    }else{
                        return "Fail";
                    }
                }else{
                    return "Failed";
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
    public String getAcceptanceStatus(int uid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "SELECT status FROM scholarship_acceptance WHERE user_id_student = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uid);
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

    @Override
    @WebMethod
    public String setAcceptance(int uid, int sid_rest, String status) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "UPDATE scholarship_acceptance SET status = ? WHERE user_id_student = ? AND scholarship_id_rest = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, status);
                stmt.setInt(2, uid);
                stmt.setInt(3, sid_rest);
                stmt.execute();

                if(stmt.getUpdateCount() > 0){
                    Logging log = new Logging("REGISTERED WITH STATUS WAITING", exchange.getRemoteAddress().getAddress().getHostAddress());
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
}
