package com.orden.soap.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class ScholarshipView {
    private int scholarship_id_rest;
    private int view_count;

    public ScholarshipView(){}

    @XmlElement
    public int getScholarship_id_rest() {
        return scholarship_id_rest;
    }
    public void setUser_id_scholarship_rest(int scholarship_id_rest) {
        this.scholarship_id_rest = scholarship_id_rest;
    }

    @XmlElement
    public int getView_count() {
        return view_count;
    }
    public void setView_count(int view_count) {
        this.view_count = view_count;
    }
}
