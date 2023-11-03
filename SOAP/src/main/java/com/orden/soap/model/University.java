/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nadil
 */
@XmlRootElement
public class University {
    private String name;
    private int php_uni_id;
    private int rest_uni_id;

    public University() {}

    public University(String name, int php_uni_id, int rest_uni_id) {
        this.name = name;
        this.php_uni_id = php_uni_id;
        this.rest_uni_id = rest_uni_id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElement
    public int getPhpUniId() {
        return php_uni_id;
    }

    public void setPhpUniId(int php_uni_id) {
        this.php_uni_id = php_uni_id;
    }

    @XmlElement
    public int getRestUniId() {
        return rest_uni_id;
    }

    public void setRestUniId(int rest_uni_id) {
        this.rest_uni_id = rest_uni_id;
    }
}