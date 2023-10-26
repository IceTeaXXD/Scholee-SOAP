/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Bookmark;
import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import javax.jws.*;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
/**
 *
 * @author Matthew
 */
@WebService(endpointInterface = "com.orden.soap.services.BookmarkService")
public class BookmarkServiceImpl implements BookmarkService {
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
    public String insertBookmark(int uid, int uis, int sid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            Bookmark b = new Bookmark(uid, uis, sid);
            if(b.insertBookmark()){
                String description = "SOAP: Bookmark Creation is Successful";
                Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                return description;
            }else{
                return "Failed to Create Bookmark";
            }
        }else{
            return "Failed to Create Bookmark";
        }
    }

    @Override
    public String deleteBookmark(int uid, int uis, int sid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            Bookmark b = new Bookmark(uid, uis, sid);
            if(b.deleteBookmark()){
                String description = "SOAP: Bookmark Deletion is Successful";
                Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();
                return description;
            }else{
                return "Failed to Delete Bookmark";
            }
        }else{
            return "Failed to Delete Bookmark";
        }
    }

    @Override
    public ArrayList<Bookmark> getBookmarkStudent(int uid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            Bookmark b = new Bookmark(uid, 0,0);
            ArrayList<Bookmark> result = b.getBookmarkStudent();
            String description = "SOAP: Get Bookmark for Student is Successful";
            Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
            log.insertLogging();
            return result;
        }else{
            return null;
        }
    }

    @Override
    public ArrayList<Bookmark> getBookmarkScholarship(int uis) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            Bookmark b = new Bookmark(0, uis,0);
            ArrayList<Bookmark> result = b.getBookmarkAdmin();
            String description = "SOAP: Get Bookmark for Admin is Successful";
            Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
            log.insertLogging();
            return result;
        }else{
            return null;
        }
    }
    
}
