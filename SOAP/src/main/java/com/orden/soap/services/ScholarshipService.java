package com.orden.soap.services;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.orden.soap.model.Scholarship;

@WebService
public interface ScholarshipService {
    @WebMethod
    public String registerScholarship(
        @WebParam(name = "user_id_scholarship_php") int uis_php,
        @WebParam(name = "scholarship_id_php") int sid_php
    );

    @WebMethod
    public String setRESTscholarshipID(
        @WebParam(name = "user_id_scholarship_php") int uis_php,
        @WebParam(name = "scholarship_id_php") int sid_php,
        @WebParam(name = "user_id_scholarship_rest") int uis_rest,
        @WebParam(name = "scholarship_id_rest") int sid_rest
    );

    @WebMethod
    public ArrayList<Scholarship> getAllScholarship();

    @WebMethod
    public void addScholarshipView(
        @WebParam(name = "user_id_scholarship_php") int uis_php,
        @WebParam(name = "scholarship_id_php") int sid_php
    );
}
