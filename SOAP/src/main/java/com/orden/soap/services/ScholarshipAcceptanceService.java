/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.orden.soap.services;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.orden.soap.model.Acceptance;

/**
 *
 * @author Matthew
 */
@WebService
public interface ScholarshipAcceptanceService {
    @WebMethod
    public String registerScholarshipApplication(
            @WebParam(name="user_id_student")int uid, 
            @WebParam(name="user_id_scholarship")int uis, 
            @WebParam(name="scholarship_id")int sid);

    @WebMethod
    public String setAcceptance(
            @WebParam(name="user_id_student")int uid, 
            @WebParam(name="scholarship_id_rest")int sid_rest,
            @WebParam(name="status") String status);

    @WebMethod
    public ArrayList<Acceptance> getAcceptanceStatus(
            @WebParam(name="user_id_student")int uid);

    @WebMethod
    public String setScholarshipIDREST(
            @WebParam(name="user_id_scholarship_php") int uid_php,
            @WebParam(name="scholarship_id_php") int sid_php,
            @WebParam(name="scholarship_id_rest") int sid_rest
    );

    @WebMethod
    public ArrayList<Acceptance> getAllScholarshipAcceptance();
}
