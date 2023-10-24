/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Bookmark;
import com.orden.soap.model.Logging;
import jakarta.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
/**
 *
 * @author Matthew
 */
public class BookmarkServiceImpl implements BookmarkService {
    @Resource
    WebServiceContext wsContext;
    
    @Override
    public String insertBookmark(int uid, int uis, int sid) {
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST); 
        Bookmark b = new Bookmark(uid, uis, sid);
        b.insertBookmark();
        String description = "Bookmark Creation is Successful";
        Logging log = new Logging(description, req.getRemoteAddr());
        log.insertLogging();
        return description;
    }

    @Override
    public String deleteBookmark(int uid, int uis, int sid) {
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST); 
        Bookmark b = new Bookmark(uid, uis, sid);
        b.deleteBookmark();
        String description = "Bookmark Deletion is Successful";
        Logging log = new Logging(description, req.getRemoteAddr());
        log.insertLogging();
        return description;
    }

    @Override
    public String getBookmarkStudent(int uid) {
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST); 
        Bookmark b = new Bookmark(uid, 0,0);
        b.getBookmarkStudent();
        String description = "Get Bookmark for Student is Successful";
        Logging log = new Logging(description, req.getRemoteAddr());
        log.insertLogging();
        return description;
    }

    @Override
    public String getBookmarkScholarship(int uis) {
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST); 
        Bookmark b = new Bookmark(0, uis,0);
        b.getBookmarkAdmin();
        String description = "Get Bookmark for Admin is Successful";
        Logging log = new Logging(description, req.getRemoteAddr());
        log.insertLogging();
        return description;
    }
    
}
