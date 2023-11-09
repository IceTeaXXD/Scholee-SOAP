package com.orden.soap.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Scholarship {
    private int user_id_scholarship_php;
    private int scholarship_id_php;
    private int user_id_scholarship_rest;
    private int scholarship_id_rest;

    public Scholarship(){}
    
    @XmlElement
    public int getUser_id_scholarship_php() {
        return user_id_scholarship_php;
    }
    public void setUser_id_scholarship_php(int user_id_scholarship_php) {
        this.user_id_scholarship_php = user_id_scholarship_php;
    }

    @XmlElement
    public int getScholarship_id_php() {
        return scholarship_id_php;
    }
    public void setScholarship_id_php(int scholarship_id_php) {
        this.scholarship_id_php = scholarship_id_php;
    }

    @XmlElement
    public int getUser_id_scholarship_rest() {
        return user_id_scholarship_rest;
    }
    public void setUser_id_scholarship_rest(int user_id_scholarship_rest) {
        this.user_id_scholarship_rest = user_id_scholarship_rest;
    }

    @XmlElement
    public int getScholarship_id_rest() {
        return scholarship_id_rest;
    }
    public void setScholarship_id_rest(int scholarship_id_rest) {
        this.scholarship_id_rest = scholarship_id_rest;
    }

}
