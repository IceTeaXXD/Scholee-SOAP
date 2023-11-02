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
public interface OrganizationRegistrationService {
    @WebMethod
    public String registerOrganization(
            @WebParam(name="org_id_php")int org_id_php
    );
    
    @WebMethod
    public String createRESTId(
            @WebParam(name="org_id_rest")int org_id_rest,
            @WebParam(name="referral_code")String referral
    );

    @WebMethod
    public String validateReferralCode(
            @WebParam(name="referral_code")String referral
    );
}
