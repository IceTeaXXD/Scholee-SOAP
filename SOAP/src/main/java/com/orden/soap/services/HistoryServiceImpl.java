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
@WebService(endpointInterface="com.orden.soap.services.HistoryService")
public class HistoryServiceImpl implements HistoryService{
    @Resource
    WebServiceContext wsContext;
    
    public HistoryServiceImpl() {
        super();
    }
    
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
    public String addHistory(int uid, int uis, int sid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            
            Logging log = new Logging("SOAP: HISTORY ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
            log.insertLogging();
            
            String query = "INSER INTO view_history VALUES (?,?,?)";
            PreparedStatement stmt;
            try {
                Database db = new Database();
                stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(3, sid);
                stmt.setInt(2, uis);
                stmt.setInt(1, uid);
                if(stmt.execute()){
                    return "Sukses menambahkan riwayat";
                }else{
                    return "Gagal menambahkan riwayat";
                }
            } catch (SQLException ex) {
                Logger.getLogger(HistoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Gagal menambahkan riwayat";
            }
        }else{
            return "API KEY SALAH";
        }
    }
    
}
