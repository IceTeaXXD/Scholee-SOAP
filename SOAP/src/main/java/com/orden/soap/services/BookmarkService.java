/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.orden.soap.services;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Matthew
 */
@WebService
public interface BookmarkService {
    @WebMethod
    public String insertBookmark(int uid, int uis, int sid);
    
    @WebMethod
    public String deleteBookmark(int uid, int uis, int sid);
    
    @WebMethod
    public String getBookmarkStudent(int uid);
    
    @WebMethod
    public String getBookmarkScholarship(int uis);
}
