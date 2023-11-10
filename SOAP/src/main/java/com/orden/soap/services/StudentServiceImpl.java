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
import com.orden.soap.model.BaseService;

/**
 *
 * @author henryanand
 */
@WebService(endpointInterface="com.orden.soap.services.StudentService")
public class StudentServiceImpl extends BaseService implements StudentService{
    private static final Database db = new Database();
    
    @Resource
    WebServiceContext wsContext;
    
    @Override
    @WebMethod
    public String registerStudent(int user_id, int rest_uni_id, int php_uni_id) {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if (validateAPIKey()) {
            try {
                String query = "INSERT INTO students (user_id, rest_uni_id, php_uni_id) VALUES (?,?,?)";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, user_id);
                stmt.setInt(2, rest_uni_id);
                stmt.setInt(3, php_uni_id);

                stmt.execute();

                String returnVal;
                if (stmt.getUpdateCount() > 0) {
                    /* TODO: Add SOAP or REST Information on Description */
                    String clientAddr = exchange.getRemoteAddress().getAddress().getHostAddress();
                    System.out.println(clientAddr);
                    Logging log = new Logging(getSource() +  " : REGISTRATION ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
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
