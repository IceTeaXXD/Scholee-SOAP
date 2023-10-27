/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.orden.soap.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Matthew
 */
@WebService
public interface HistoryService {
    @WebMethod
    public String addHistory(
            @WebParam(name="user_id_student") int uid,
            @WebParam(name="user_id_scholarship") int uis,
            @WebParam(name="scholarship_id") int sid
    );
}
