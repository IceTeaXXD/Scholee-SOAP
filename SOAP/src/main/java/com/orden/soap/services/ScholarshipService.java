package com.orden.soap.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface ScholarshipService {
    @WebMethod
    public String registerScholarshipApplication(
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
}
