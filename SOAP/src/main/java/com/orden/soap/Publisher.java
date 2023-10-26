/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap;

import com.orden.soap.services.BookmarkServiceImpl;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Matthew
 */
public class Publisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ws/bookmarking", new BookmarkServiceImpl());
        System.out.println("Listening at port 8080");
    }
}
