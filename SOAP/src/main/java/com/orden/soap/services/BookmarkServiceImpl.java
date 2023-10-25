/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Bookmark;
import com.orden.soap.model.Logging;
import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceContext;
/**
 *
 * @author Matthew
 */
@WebService(endpointInterface = "com.orden.soap.services.BookmarkService")
public class BookmarkServiceImpl implements BookmarkService {
    @Resource
    WebServiceContext wsContext;
    
    @Override
    public String insertBookmark(int uid, int uis, int sid) {
        MessageContext mc = wsContext.getMessageContext();
        Bookmark b = new Bookmark(uid, uis, sid);
        b.insertBookmark();
        String description = "Bookmark Creation is Successful";
        Logging log = new Logging(description, "192.168.0.1");
        log.insertLogging();
        return description;
    }

    @Override
    public String deleteBookmark(int uid, int uis, int sid) {
        MessageContext mc = wsContext.getMessageContext();
        Bookmark b = new Bookmark(uid, uis, sid);
        b.deleteBookmark();
        String description = "Bookmark Deletion is Successful";
        Logging log = new Logging(description, "192.168.0.1");
        log.insertLogging();
        return description;
    }

    @Override
    public Bookmark getBookmarkStudent(int uid) {
        MessageContext mc = wsContext.getMessageContext();
        Bookmark b = new Bookmark(uid, 0,0);
        b.getBookmarkStudent();
        String description = "Get Bookmark for Student is Successful";
        Logging log = new Logging(description, "192.168.0.1");
        log.insertLogging();
        return b;
    }

    @Override
    public String getBookmarkScholarship(int uis) {
        MessageContext mc = wsContext.getMessageContext();
        Bookmark b = new Bookmark(0, uis,0);
        b.getBookmarkAdmin();
        String description = "Get Bookmark for Admin is Successful";
        Logging log = new Logging(description, "192.168.0.1");
        log.insertLogging();
        return description;
    }
    
}
