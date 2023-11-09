package com.orden.soap.services;

import com.orden.soap.database.Database;
import com.orden.soap.model.Logging;
import com.orden.soap.model.Scholarship;
import com.sun.net.httpserver.HttpExchange;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

@WebService(endpointInterface = "com.orden.soap.services.ScholarshipService")
public class ScholarshipServiceImpl implements ScholarshipService{
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
    public String registerScholarship(int uis_php, int sid_php) {
        MessageContext mc = wsContext.getMessageContext();    
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if(validateAPIKey()){
            /* Select from Organization Registration. Check if organization has registered or not */
            try {
                String query = "SELECT org_id_rest FROM organization_registration WHERE org_id_php = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis_php);

                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.next() && resultSet.getInt("org_id_rest") != -1){
                    /* Insert into Scholarship */
                    query = "INSERT INTO scholarship (user_id_scholarship_php, scholarship_id_php, user_id_scholarship_rest) " +
                            "VALUES (?,?,?)";
                    stmt = db.getConnection().prepareStatement(query);
                    stmt.setInt(1, uis_php);
                    stmt.setInt(2, sid_php);
                    stmt.setInt(3, resultSet.getInt("org_id_rest"));

                    stmt.execute();

                    /* Check if the execution is successful or not */
                    if(stmt.getUpdateCount() > 0){
                        Logging log = new Logging("SCHOLARSHIP ADDED", exchange.getRemoteAddress().getAddress().getHostAddress());
                        log.insertLogging();
                        return "Scholarship Added";
                    }else{
                        return "Failed";
                    }
                }else{
                    return "You haven't activated your account";
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return "Failed";
            }
        }else{
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    public String setRESTscholarshipID(int uis_php, int sid_php, int uis_rest, int sid_rest) {
        MessageContext mc = wsContext.getMessageContext();    
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if(validateAPIKey()){
            try{
                /* Validate if sid_rest is -1 or not */
                String query = "SELECT scholarship_id_rest FROM scholarship";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.next() && resultSet.getInt("scholarship_id_rest") == -1){
                    query = "UPDATE scholarship SET scholarship_id_rest = ? WHERE user_id_scholarship_php = ? AND scholarship_id_php = ? AND user_id_scholarship_rest = ?";
                    stmt = db.getConnection().prepareStatement(query);
                    stmt.setInt(1, sid_rest);
                    stmt.setInt(2, uis_php);
                    stmt.setInt(3, sid_php);
                    stmt.setInt(4, uis_rest);

                    stmt.execute();

                    if(stmt.getUpdateCount() > 0){
                        Logging log = new Logging("REST Scholarship SET", exchange.getRemoteAddress().getAddress().getHostAddress());
                        log.insertLogging();
                        return "REST Scholarship SET";
                    }else{
                        return "Failed";
                    }
                }else{
                    return "Failed";
                }
            }catch (SQLException e){
                e.printStackTrace();
                return "Failed";
            }
        }else{
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    public ArrayList<Scholarship> getAllScholarship() {
        MessageContext mc = wsContext.getMessageContext();    
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if(validateAPIKey()){
            try{
                Logging log = new Logging("SCHOLARSHIP GET ALL",
                        exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();

                String query = "SELECT * FROM scholarship";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                ResultSet resultSet = stmt.executeQuery();

                ArrayList<Scholarship> scholarships = new ArrayList<>();
                while(resultSet.next()){
                    Scholarship scholarship = new Scholarship();
                    scholarship.setScholarship_id_php(resultSet.getInt("scholarship_id_php"));
                    scholarship.setScholarship_id_rest(resultSet.getInt("scholarship_id_rest"));
                    scholarship.setUser_id_scholarship_php(resultSet.getInt("user_id_scholarship_php"));
                    scholarship.setUser_id_scholarship_rest(resultSet.getInt("user_id_scholarship_rest"));

                    scholarships.add(scholarship);
                }
                return scholarships;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }else{
            return null; 
        }
    }
    
}
