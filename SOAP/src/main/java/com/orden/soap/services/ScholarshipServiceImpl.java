package com.orden.soap.services;

import com.orden.soap.model.Logging;
import com.orden.soap.model.Scholarship;
import com.orden.soap.model.ScholarshipView;
import com.sun.net.httpserver.HttpExchange;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.handler.MessageContext;
import com.orden.soap.model.BaseService;

@WebService(endpointInterface = "com.orden.soap.services.ScholarshipService")
public class ScholarshipServiceImpl extends BaseService implements ScholarshipService{
    @Override
    @WebMethod
    public String registerScholarship(int uis_php, int sid_php) {
        MessageContext mc = wsContext.getMessageContext();    
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        if(validateAPIKey()){
            try {
                String query = "SELECT org_id_rest FROM organization_registration WHERE org_id_php = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis_php);

                ResultSet resultSet = stmt.executeQuery();
                
                if(resultSet.next()){
                    query = "INSERT INTO scholarship (user_id_scholarship_php, scholarship_id_php, user_id_scholarship_rest) " +
                            "VALUES (?,?,?)";
                    stmt = db.getConnection().prepareStatement(query);
                    stmt.setInt(1, uis_php);
                    stmt.setInt(2, sid_php);
                    stmt.setInt(3, resultSet.getInt("org_id_rest"));

                    stmt.execute();

                    /* Check if the execution is successful or not */
                    if(stmt.getUpdateCount() > 0){
                        Logging log = new Logging("registerScholarship",
                                                "REQUEST-SERVICE: " + getSource() + "; uis_php: " + uis_php + "; sid_php: " + sid_php,
                                                exchange.getRemoteAddress().getAddress().getHostAddress());
                        log.insertLogging();
                        return "Scholarship Added";
                    }else{
                        return "Failed";
                    }
                }else{
                    return "There is no organization with that info";
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
                String query = "SELECT scholarship_id_rest FROM scholarship WHERE user_id_scholarship_php = ? AND scholarship_id_php = ? AND user_id_scholarship_rest = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis_php);
                stmt.setInt(2, sid_php);
                stmt.setInt(3, uis_rest);
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
                        Logging log = new Logging("setRESTScholarshipID",
                                    "REQUEST-SERVICE: " + getSource() + "; uis_php: " + uis_php + "; sid_php: " + sid_php + "; uis_rest: " + uis_rest + "; sid_rest: " + sid_rest, 
                                    exchange.getRemoteAddress().getAddress().getHostAddress());
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

                Logging log = new Logging("getAllScholarship",
                                    "REQUEST-SERVICE: " + getSource(),
                                    exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();

                return scholarships;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }else{
            return null; 
        }
    }

    @Override
    @WebMethod
    public void addScholarshipView(int uis_php, int sid_php){
        MessageContext mc = wsContext.getMessageContext();    
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");

        if(validateAPIKey()){
            try{
                String query = "UPDATE scholarship SET view_count = view_count + 1 WHERE user_id_scholarship_php = ? AND scholarship_id_php = ?";

                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis_php);
                stmt.setInt(2, sid_php);

                if(stmt.executeUpdate() > 0){
                    Logging log = new Logging("addScholarshipView",
                                        "REQUEST-SERVICE: " + getSource() + "; uis_php: " + uis_php + "; sid_php: " + sid_php,
                                        exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    @WebMethod
    public ArrayList<ScholarshipView> getScholarshipView(int uis_rest){
        MessageContext mc = wsContext.getMessageContext();    
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");

        if(validateAPIKey()){
            try{
                String query = "SELECT scholarship_id_rest, view_count FROM scholarship WHERE user_id_scholarship_rest = ? AND user_id_scholarship_rest != -1 AND scholarship_id_rest != -1";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis_rest);

                ResultSet rs = stmt.executeQuery();

                ArrayList<ScholarshipView> list = new ArrayList<>();

                while(rs.next()){
                    ScholarshipView sv = new ScholarshipView();
                    sv.setUser_id_scholarship_rest(rs.getInt("scholarship_id_rest"));
                    sv.setView_count(rs.getInt("view_count"));

                    list.add(sv);
                }

                Logging log = new Logging("getScholarshipView",
                                        "REQUEST-SERVICE: " + getSource() + "; uis_rest: " + uis_rest,
                                        exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                
                return list;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }else{
            return null;
        }
    }
    
}
