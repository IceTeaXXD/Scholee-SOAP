/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.model;

import com.orden.soap.database.Database;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthew
 */
public class Bookmark extends Database {
    private int user_id_student;
    private int user_id_scholarship;
    private int scholarship_id;

    public Bookmark(int user_id_student, int user_id_scholarship, int scholarship_id) {
        super();
        this.user_id_student = user_id_student;
        this.user_id_scholarship = user_id_scholarship;
        this.scholarship_id = scholarship_id;
    }
   
    public void insertBookmark(){
        try {
            Statement stmt = this.conn.createStatement();
            String query = "INSERT INTO bookmark (user_id_student, user_id_scholarship, scholarship_id)"
                    + " VALUES (" + this.user_id_student + "," + this.user_id_scholarship + "," + this.scholarship_id + ")";
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(Bookmark.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
