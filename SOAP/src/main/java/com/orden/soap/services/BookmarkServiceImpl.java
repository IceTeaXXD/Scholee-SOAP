/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Bookmark;
import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceContext;
import java.util.ArrayList;
import java.util.Arrays;
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
            System.out.println(exchange.getLocalAddress());
            System.out.println(exchange.getProtocol());
            System.out.println(exchange.getRemoteAddress().getAddress().getHostAddress());
            String apiKey = exchange.getRequestHeaders().toString();
            System.out.println(apiKey);
            
            if (apiKey != null && apiKey.equals("YourExpectedAPIKey")) {
                System.out.println("API-KEY: " + apiKey);
                return true;
            } else {
                System.out.println("Invalid API-KEY: " + apiKey);
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle exceptions as needed
        }
    }
    
    @Override
    public String insertBookmark(int uid, int uis, int sid) {
        if(validateAPIKey()){
            Bookmark b = new Bookmark(uid, uis, sid);
            b.insertBookmark();
            String description = "Bookmark Creation is Successful";
            Logging log = new Logging(description, "192.168.0.1");
            log.insertLogging();
            return description;
        }else{
            return "Failed to Create Bookmark";
        }
    }

    @Override
    public String deleteBookmark(int uid, int uis, int sid) {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        Bookmark b = new Bookmark(uid, uis, sid);
        b.deleteBookmark();
        String description = "Bookmark Deletion is Successful";
        Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
        log.insertLogging();
        return description;
    }

    @Override
    public ArrayList<Bookmark> getBookmarkStudent(int uid) {
        if(validateAPIKey()){
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            Bookmark b = new Bookmark(uid, 0,0);
            ArrayList<Bookmark> result = b.getBookmarkStudent();
            String description = "Get Bookmark for Student is Successful";
            Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
            log.insertLogging();
            return result;
        }else{
            return null;
        }
    }

    @Override
    public String getBookmarkScholarship(int uis) {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        Bookmark b = new Bookmark(0, uis,0);
        b.getBookmarkAdmin();
        String description = "Get Bookmark for Admin is Successful";
        Logging log = new Logging(description, exchange.getRemoteAddress().getAddress().getHostAddress());
        log.insertLogging();
        return description;
    }
    
}
