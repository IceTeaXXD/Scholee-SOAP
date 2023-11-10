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
import java.sql.ResultSet;
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
 * @author Matthew
 */
@WebService(endpointInterface="com.orden.soap.services.OrganizationRegistrationService")
public class OrganizationRegistrationServiceImpl implements OrganizationRegistrationService {
    
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
    public String registerOrganization(int org_id_php) {
        if(validateAPIKey()){
            String query = "INSERT INTO organization_registration (org_id_php, referral_code) VALUES (?,?)";
            String returnVal;
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                int tokenLength = 16;

                SecureRandom secureRandom = new SecureRandom();
                byte[] randomBytes = new byte[tokenLength];
                secureRandom.nextBytes(randomBytes);

                String token = Base64.getEncoder().encodeToString(randomBytes);
                
                stmt.setInt(1, org_id_php);
                stmt.setString(2, token);
                stmt.execute();

                if(stmt.getUpdateCount() > 0){
                    /* TODO: Add SOAP or REST Information on Description */
                    Logging log = new Logging("REGISTRATION ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    returnVal = "Register Success";
                }else{
                    returnVal = "Register Unsuccessful";
                }

                return returnVal;
            } catch (SQLException ex) {
                Logger.getLogger(OrganizationRegistrationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Failed";
            }
        }else{
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    /*
     * @Param
     * org_id_rest: ID dari Organization yang digenerate di REST Service
    */
    public String createRESTId(int org_id_rest, String referral) {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if(validateAPIKey()){
            try {
                String query = "UPDATE organization_registration SET org_id_rest = ? WHERE referral_code = ? AND org_id_rest = -1";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, org_id_rest);
                stmt.setString(2, referral);
                stmt.execute();
                
                String returnVal;
                
                if(stmt.getUpdateCount() > 0){
                    Logging log = new Logging("HISTORY ADD", exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                    returnVal = "Register Success";

                    /* Kalau sudah punya scholarship update dengan org_id_rest */
                    query = "SELECT org_id_php FROM organization_registration WHERE org_id_rest = ?";
                    stmt = db.getConnection().prepareStatement(query);
                    stmt.setInt(1, org_id_rest);

                    ResultSet rs = stmt.executeQuery();

                    if(rs.next()){
                        query = "UPDATE scholarship SET user_id_scholarship_rest = ? WHERE user_id_scholarship_php = ?";
                        stmt = db.getConnection().prepareStatement(query);
                        stmt.setInt(1, org_id_rest);
                        stmt.setInt(2, rs.getInt("org_id_php"));

                        stmt.execute();
                    }

                }else{
                    returnVal = "Register Unsuccessful";
                }
                
                return returnVal;
            } catch (SQLException ex) {
                Logger.getLogger(OrganizationRegistrationServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            return "Illegal Process";
        }
        return "Failed";
    }

    @Override
    @WebMethod
    public String validateReferralCode(String referral) {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if(validateAPIKey()){
            try{
                String query = "SELECT org_id_php FROM organization_registration WHERE referral_code = (?) AND org_id_rest = -1";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, referral);
                ResultSet resultSet = stmt.executeQuery();
                Logging log = new Logging("Checking Referral Code", exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                if (resultSet.next() && !resultSet.next()) {
                    // A result was found for the given referral code, and no additional rows are present
                    return "True";
                }else{
                    return "False";
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                return "Failed";
            }
        }else{
            return "Illegal Process";
        }
    }   
}
