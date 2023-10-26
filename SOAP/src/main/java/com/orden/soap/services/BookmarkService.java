/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Bookmark;

import java.util.ArrayList;
import javax.jws.*;

/**
 *
 * @author Matthew
 */
@WebService
public interface BookmarkService {
    @WebMethod
    public String insertBookmark(
            @WebParam(name="user_id_student")int uid, 
            @WebParam(name="user_id_scholarship")int uis, 
            @WebParam(name="scholarship_id")int sid
    );
    
    @WebMethod
    public String deleteBookmark(
            @WebParam(name="user_id_student")int uid, 
            @WebParam(name="user_id_scholarship")int uis, 
            @WebParam(name="scholarship_id")int sid
    );
    
    @WebMethod
    public ArrayList<Bookmark> getBookmarkStudent(
            @WebParam(name="user_id_student")int uid
    );
    
    @WebMethod
    public ArrayList<Bookmark> getBookmarkScholarship(
            @WebParam(name="user_id_scholarship")int uis
    );
}
