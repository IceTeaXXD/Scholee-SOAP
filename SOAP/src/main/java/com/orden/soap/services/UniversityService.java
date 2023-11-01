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
public interface UniversityService {
    @WebMethod
    public String createUniversity(
            @WebParam(name="rest_uni_id")int rest_uni_id
    );
    
    @WebMethod
    public String setPHPId(
            @WebParam(name="php_uni_id") int php_uni_id
    );
}
