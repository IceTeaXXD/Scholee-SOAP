/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.orden.soap.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import com.orden.soap.model.University;

/**
 *
 * @author Matthew, Nadil
 */
@WebService
public interface UniversityService {
        @WebMethod
        public String createUniversity(
                        @WebParam(name = "rest_uni_id") int rest_uni_id,
                        @WebParam(name = "university_name") String university_name);

        @WebMethod
        public String setPHPId(
                        @WebParam(name = "php_uni_id") int php_uni_id,
                        @WebParam(name = "rest_uni_id") int rest_uni_id);

        @WebMethod
        public ArrayList<University> getAllUniversities();
}
