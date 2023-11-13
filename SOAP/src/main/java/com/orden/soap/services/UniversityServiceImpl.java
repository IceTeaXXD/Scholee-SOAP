/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import com.orden.soap.model.University;
import com.orden.soap.model.BaseService;
/**
 *
 * @author Matthew
 */
@WebService(endpointInterface = "com.orden.soap.services.UniversityService")
public class UniversityServiceImpl extends BaseService implements UniversityService {
    @Override
    @WebMethod
    public String createUniversity(int rest_uni_id, String university_name) {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();

            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");

            try {
                String query = "INSERT INTO university (rest_uni_id, name) VALUES (?,?)";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, rest_uni_id);
                stmt.setString(2, university_name);
                stmt.execute();
                if (stmt.getUpdateCount() > 0) {
                    /* Log it and Return Success */
                    Logging log = new Logging("createUniversity", 
                                                "REQUEST-SERVICE: " + getSource() + "; rest_uni_id: " + rest_uni_id + "; university_name: " + university_name,
                                                exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    return "Success";
                } else {
                    return "Failed";
                }
            } catch (SQLException ex) {
                Logger.getLogger(UniversityServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Failed";
            }
        } else {
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    public String setPHPId(int php_uni_id, int rest_uni_id) {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();

            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");

            try {
                String query = "UPDATE university SET php_uni_id = ? WHERE rest_uni_id = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, php_uni_id);
                stmt.setInt(2, rest_uni_id);

                stmt.execute();
                if (stmt.getUpdateCount() > 0) {
                    Logging log = new Logging("setPHPId",
                                                "REQUEST-SERVICE: " + getSource() + "; rest_uni_id: " + rest_uni_id + "; php_uni_id: " + php_uni_id,
                                                exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    return "Success";
                } else {
                    return "Failed";
                }
            } catch (SQLException ex) {
                Logger.getLogger(UniversityServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Failed";
            }
        } else {
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    public ArrayList<University> getAllUniversities() {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();

            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");

            try {
                String query = "SELECT * FROM university";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.execute();
                ArrayList<University> universities = new ArrayList<>();
                while (stmt.getResultSet().next()) {
                    University uni = new University();
                    uni.setPhpUniId(stmt.getResultSet().getInt("php_uni_id"));
                    uni.setRestUniId(stmt.getResultSet().getInt("rest_uni_id"));
                    uni.setName(stmt.getResultSet().getString("name"));
                    universities.add(uni);
                }
                Logging log = new Logging("getAllUniversities",
                                            "REQUEST-SERVICE: " + getSource(),
                                            exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                return universities;
            } catch (SQLException ex) {
                Logger.getLogger(UniversityServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            return null;
        }
    }
}
