package com.orden.soap.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Acceptance {
    private int user_id_student;
    private int user_id_scholarship;
    private int scholarship_id;
    private String status;

    @XmlElement
    public int getUser_id_student() {
        return user_id_student;
    }

    public void setUser_id_student(int user_id_student) {
        this.user_id_student = user_id_student;
    }

    @XmlElement
    public int getUser_id_scholarship() {
        return user_id_scholarship;
    }

    public void setUser_id_scholarship(int user_id_scholarship) {
        this.user_id_scholarship = user_id_scholarship;
    }

    @XmlElement
    public int getScholarship_id() {
        return scholarship_id;
    }
    public void setScholarship_id(int scholarship_id) {
        this.scholarship_id = scholarship_id;
    }

    @XmlElement
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
