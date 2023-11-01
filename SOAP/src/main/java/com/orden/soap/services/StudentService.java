/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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
            @WebParam(name="std_id_php")int std_id_php
    );
}
