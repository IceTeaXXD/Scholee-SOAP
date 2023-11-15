/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orden.soap.services;

import com.orden.soap.model.Acceptance;
import com.orden.soap.model.Logging;
import com.sun.net.httpserver.HttpExchange;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.handler.MessageContext;
import com.orden.soap.model.BaseService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Matthew
 */
@WebService(endpointInterface = "com.orden.soap.services.ScholarshipAcceptanceService")
public class ScholarshipAcceptanceServiceImpl extends BaseService implements ScholarshipAcceptanceService {
    public String acceptedMessage = "<html><body>" +
            "<h1>Hello there!</h1>" +
            "<p>Congratulations! You have been accepted to the scholarship.</p>" +
            "<p>Thank you for your support!</p>" +
            "</body></html>";
    public String rejectedMessage = "<html><body>" +
            "<h1>Hello there!</h1>" +
            "<p>We are sorry to inform you that you have been rejected from the scholarship.</p>" +
            "<p>Thank you for your support!</p>" +
            "</body></html>";

    @Override
    @WebMethod
    public String registerScholarshipApplication(int uid, int uis, int sid) {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                /* Find the REST Scholarship_ID */
                String query = "SELECT scholarship_id_rest FROM scholarship WHERE user_id_scholarship_php = ? AND scholarship_id_php = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uis);
                stmt.setInt(2, sid);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    query = "INSERT INTO scholarship_acceptance VALUES (?,?,?,?,?)";
                    stmt = db.getConnection().prepareStatement(query);
                    stmt.setInt(1, uid);
                    stmt.setInt(2, uis);
                    stmt.setInt(3, sid);
                    stmt.setInt(4, rs.getInt("scholarship_id_rest"));
                    stmt.setString(5, "waiting");
                    stmt.execute();

                    if (stmt.getUpdateCount() > 0) {
                        Logging log = new Logging("registerScholarshipApplication",
                                "REQUEST-SERVICE: " + getSource() + "; uid: " + uid + "; uis: " + uis + "; sid: " + sid,
                                exchange.getRemoteAddress().getAddress().getHostAddress());
                        log.insertLogging();
                        return "Success";
                    } else {
                        return "Fail";
                    }
                } else {
                    return "Failed";
                }
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Fail";
            }
        } else {
            return "Illegal Process";
        }
    }

    @Override
    public ArrayList<Acceptance> getAcceptanceStatus(int uid) {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "SELECT user_id_student, user_id_scholarship_php, scholarship_id_rest, scholarship_id_php, status FROM scholarship_acceptance WHERE user_id_student = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, uid);
                ResultSet rs = stmt.executeQuery();

                ArrayList<Acceptance> acceptances = new ArrayList<>();

                while (rs.next()) {
                    Acceptance acceptance = new Acceptance();
                    acceptance.setUser_id_student(rs.getInt("user_id_student"));
                    acceptance.setUser_id_scholarship(rs.getInt("user_id_scholarship_php"));
                    acceptance.setScholarship_id_php(rs.getInt("scholarship_id_php"));
                    acceptance.setStatus(rs.getString("status"));
                    acceptance.setScholarship_id_rest(rs.getInt("scholarship_id_rest"));
                    acceptances.add(acceptance);
                }

                Logging log = new Logging("getAcceptanceStatus",
                        "REQUEST-SERVICE: " + getSource() + "; uid: " + uid,
                        exchange.getRemoteAddress().getAddress().getHostAddress());
                log.insertLogging();

                return acceptances;
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @WebMethod
    public String setAcceptance(int uid, int sid_rest, String sname, String status) {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "UPDATE scholarship_acceptance SET status = ? WHERE user_id_student = ? AND scholarship_id_rest = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setString(1, status);
                stmt.setInt(2, uid);
                stmt.setInt(3, sid_rest);
                stmt.execute();

                if (stmt.getUpdateCount() > 0) {
                    Logging log = new Logging("setAcceptance",
                            "REQUEST-SERVICE: " + getSource() + "; uid: " + uid + "; sid_rest: " + sid_rest
                                    + "; status: " + status,
                            exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();

                    ArrayList<String> userInfo = getUserInfo(uid);
                    String name = userInfo.get(0);
                    String email = userInfo.get(1);
                    sendEmail(email, name, status, sname);
                    return "Success";
                } else {
                    return "Fail";
                }
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Fail";
            }
        } else {
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    public String setScholarshipIDREST(int uid_php, int sid_php, int sid_rest) {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "UPDATE scholarship_acceptance SET scholarship_id_rest = ? WHERE user_id_scholarship_php = ? AND scholarship_id_php = ?";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                stmt.setInt(1, sid_rest);
                stmt.setInt(2, uid_php);
                stmt.setInt(3, sid_php);
                stmt.execute();
                if (stmt.getUpdateCount() > 0) {
                    Logging log = new Logging("setScholarshipIDREST",
                            "REQUEST-SERVICE: " + getSource() + "; uid_php: " + uid_php + "; sid_php: " + sid_php
                                    + "; sid_rest: " + sid_rest,
                            exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();

                    return "Success";
                } else {
                    return "Fail";
                }
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return "Fail";
            }
        } else {
            return "Illegal Process";
        }
    }

    @Override
    @WebMethod
    public ArrayList<Acceptance> getAllScholarshipAcceptance() {
        if (validateAPIKey()) {
            MessageContext mc = wsContext.getMessageContext();
            HttpExchange exchange = (HttpExchange) mc.get("com.sun.xml.ws.http.exchange");
            try {
                String query = "SELECT user_id_student, user_id_scholarship_php, scholarship_id_rest, scholarship_id_php, status FROM scholarship_acceptance";
                PreparedStatement stmt = db.getConnection().prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                ArrayList<Acceptance> acceptances = new ArrayList<>();

                while (rs.next()) {
                    Acceptance acceptance = new Acceptance();
                    acceptance.setUser_id_student(rs.getInt("user_id_student"));
                    acceptance.setUser_id_scholarship(rs.getInt("user_id_scholarship_php"));
                    acceptance.setScholarship_id_php(rs.getInt("scholarship_id_php"));
                    acceptance.setStatus(rs.getString("status"));
                    acceptance.setScholarship_id_rest(rs.getInt("scholarship_id_rest"));

                    Logging log = new Logging("getAllScholarshipAcceptanc",
                            "REQUEST-SERVICE: " + getSource(),
                            exchange.getRemoteAddress().getAddress().getHostAddress());
                    log.insertLogging();

                    acceptances.add(acceptance);
                }

                return acceptances;
            } catch (SQLException ex) {
                Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    public void sendEmail(String to, String name, String status, String scholarshipname) {
        String from = "scholeeedu@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("scholeeedu@gmail.com", "pnpaxnrwarupnojb");
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Scholarship Application Announcement");
            if (status == "accepted") {
                message.setContent(
                        "<html><body><h1>Dear" + name + "</h1><p>Congratulations! You have been accepted on the "
                                + scholarshipname + "</p><p>Thank you for your application!</p></body></html>",
                        "text/html");
            } else {
                message.setContent(
                        "<html><body><h1>Dear" + name + "</h1><p>We are sorry to inform you that you have been rejected on the "
                                + scholarshipname + "</p><p>Thank you for your application!</p></body></html>",
                        "text/html");
            }
            System.out.println("Sending email...");
            Transport.send(message);
            System.out.println("Email sent!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    @Override
    @WebMethod
    public String test() {
        String to = "nadilahmad13@gmail.com";
        String from = "scholeeedu@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("scholeeedu@gmail.com", "pnpaxnrwarupnojb");
            }
        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Dasar Lab Admin!");

            // Add header to email content
            String content = """
                        <html>
                            <body>
                                <h1>Hello Jason!</h1>
                                <h2>Congratulations!</h2>
                                <p>You have been accepted to Nadil's Scholarship.</p>
                                <p>Thank you for your support!</p>
                            </body>
                            <footer>
                                <p>© 2019 Nadil's Scholarship</p>
                            </footer>
                        </html>
                    """;
            message.setContent(content, "text/html");
            System.out.println("Sending email...");
            Transport.send(message);
            System.out.println("Email sent!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return "Success";
    }

    @Override
    @WebMethod
    public ArrayList<String> getUserInfo(int userid) {
        String url = dotenv.get("PHP_URL");
        String endpoint = url + "/api/profile/info.php?userid=" + String.valueOf(1);
        try {
            URI uri;
            uri = new URI(endpoint);
            URL obj = uri.toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("Sending 'GET' request to URL : " + endpoint);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String name = response.toString().split(",")[0].split(":")[1].replace("\"", "");
            String email = response.toString().split(",")[1].split(":")[1].replace("\"", "");
            // if the last char of email is "}" remove it
            if (email.charAt(email.length() - 1) == '}') {
                email = email.substring(0, email.length() - 1);
            }
            ArrayList<String> userInfo = new ArrayList<>();
            userInfo.add(name);
            userInfo.add(email);
            return userInfo;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } catch (IOException ex) {
            Logger.getLogger(ScholarshipAcceptanceServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
