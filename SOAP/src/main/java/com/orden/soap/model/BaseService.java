/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;
import com.sun.net.httpserver.HttpExchange;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import io.github.cdimascio.dotenv.Dotenv;
import javax.annotation.Resource;

/**
 *
 * @author henryanand
 */
public class BaseService {
    Dotenv dotenv = Dotenv.load();
    @Resource
    WebServiceContext wsContext;
    public boolean validateAPIKey() {
        try {
            MessageContext mc = wsContext.getMessageContext();

            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");

            if (apiKey.equals(dotenv.get("REST_API_KEY")) || apiKey.equals(dotenv.get("PHP_API_KEY"))) {
                System.out.println("X-API-KEY: " + apiKey);
                return true;
            } else {
                System.out.println("Invalid X-API-KEY: " + apiKey);
                return false;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public String getSource() {
        MessageContext mc = wsContext.getMessageContext();
        HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
        String apiKey = exchange.getRequestHeaders().getFirst("X-API-KEY");
        if (apiKey.equals(dotenv.get("REST_API_KEY"))) {
            return "REST";
        } else {
            return "PHP";
        }
    }
}
