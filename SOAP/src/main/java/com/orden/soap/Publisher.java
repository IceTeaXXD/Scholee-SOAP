/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap;

import com.orden.soap.services.OrganizationRegistrationServiceImpl;
import com.orden.soap.services.ScholarshipAcceptanceServiceImpl;
import com.orden.soap.services.ScholarshipServiceImpl;
import com.orden.soap.services.UniversityServiceImpl;
import com.orden.soap.services.StudentServiceImpl;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Matthew
 */
public class Publisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ws/OrganizationRegistration", new OrganizationRegistrationServiceImpl());
        Endpoint.publish("http://localhost:8080/ws/UniversityService", new UniversityServiceImpl());
        Endpoint.publish("http://localhost:8080/ws/ScholarshipAcceptance", new ScholarshipAcceptanceServiceImpl());
        Endpoint.publish("http://localhost:8080/ws/StudentService", new StudentServiceImpl());
        Endpoint.publish("http://localhost:8080/ws/ScholarshipService", new ScholarshipServiceImpl());
        System.out.println("Listening at port 8080");
    }
}
