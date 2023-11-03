/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
/**
 *
 * @author henryanand
 */
@WebService
public interface StudentService {
    @WebMethod
    public String registerStudent(
            @WebParam(name="user_id")int user_id,
            @WebParam(name="rest_uni_id")int rest_uni_id,
            @WebParam(name="php_uni_id")int php_uni_id
    );
}
