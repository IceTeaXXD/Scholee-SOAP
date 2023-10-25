/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;

import com.orden.soap.database.Database;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthew
 */
@XmlRootElement
public class Bookmark extends Database {
    private int user_id_student;
    private int user_id_scholarship;
    private int scholarship_id;

    public Bookmark() {
        super();
        this.user_id_student = 0;
        this.user_id_scholarship = 0;
        this.scholarship_id = 0;
    }

    public Bookmark(int user_id_student, int user_id_scholarship, int scholarship_id) {
        super();
        this.user_id_student = user_id_student;
        this.user_id_scholarship = user_id_scholarship;
        this.scholarship_id = scholarship_id;
    }
    
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

    public void insertBookmark(){
        try {
            Statement stmt = this.conn.createStatement();
            String query = "INSERT INTO bookmark (user_id_student, user_id_scholarship, scholarship_id, priority)"
                    + " VALUES (" + this.user_id_student + "," + this.user_id_scholarship + "," + this.scholarship_id + ", 1)";
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(Bookmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteBookmark(){
        try{
            Statement stmt = this.conn.createStatement();
            String query = "DELETE FROM bookmark WHERE user_id_student = " +
                            this.user_id_student + " AND scholarship_id = " + this.scholarship_id +
                            " AND user_id_scholarship = " + this.user_id_scholarship;
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(Bookmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Bookmark> getBookmarkStudent(){
        try{
            String query = "SELECT * FROM bookmark WHERE user_id_student = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, this.user_id_student);
            ResultSet res = stmt.executeQuery();
            ArrayList<Bookmark> b = new ArrayList<>();
            while (res.next()) {
                Bookmark bookmark = new Bookmark(res.getInt("user_id_student"), res.getInt("user_id_scholarship"), res.getInt("scholarship_id"));
                b.add(bookmark);
            }
            return b;
        } catch (SQLException ex) {
            Logger.getLogger(Bookmark.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void getBookmarkAdmin(){
        try{
            String query = "SELECT * FROM bookmark WHERE user_id_scholarship = ?";
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, this.user_id_scholarship);
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Bookmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
