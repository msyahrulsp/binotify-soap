package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.NewSubscriberInterface;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.xml.ws.WebServiceContext;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

@WebService(endpointInterface = "com.binotify.interfaces.NewSubscriberInterface")
public class NewSubscriberController extends Database implements NewSubscriberInterface {
    @Resource
    WebServiceContext wsContext;

    @WebMethod
    public boolean newSubscribe(@WebParam(name = "creator_id") int creator_id,
            @WebParam(name = "subscriber_id") int subscriber_id) {
        if (verifyAPIKey(wsContext)) {
            String query = "insert into subscription (creator_id, subscriber_id, status) values ('" + creator_id
                    + "', '" + subscriber_id + "', 'PENDING')";
            try {
                int res = this.executeUpdate(query);
                if (res != 0) {
                    this.sendEmail(creator_id, subscriber_id);
                    insertLog(wsContext, "Mendapatkan data subscription baru", "/subscribe");
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                return false;
            }
        } else {
            return false;
        }
    }

    public String getAdmin() {
        try {
            URL url = new URL(System.getenv("REST_API_URL") + "/admin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed :HTTP error code :" + conn.getResponseCode());
            }

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            scanner.close();
            conn.disconnect();

            String res = "";
            int emailStartIdx = inline.indexOf("email") + 8;
            for (int i = emailStartIdx; inline.charAt(i) != ','; i++) {
                if (inline.charAt(i) != '"') {
                    res += inline.charAt(i);
                }
            }
            res += "|";
            int nameStartIdx = inline.indexOf("name") + 7;
            for (int i = nameStartIdx; inline.charAt(i) != '}'; i++) {
                if (inline.charAt(i) != '"') {
                    res += inline.charAt(i);
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);

            // Fallback Email
            return "admin@gmail.com";
        }
    }

    public void sendEmail(int creator_id, int subscriber_id) {
        Properties prop = new Properties();
        if (System.getenv("MAIL_MODE").equals("dev")) {
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.mailtrap.io");
            prop.put("mail.smtp.port", "2525");
            prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
        } else {
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (System.getenv("MAIL_MODE").equals("dev")) {
                    return new PasswordAuthentication("b420a2e34fcc6a", "479fc24e1e2b1a");
                } else {
                    return new PasswordAuthentication("mssp892@gmail.com", System.getenv("MAIL_PASSWORD"));
                }
            }
        });

        Message message = new MimeMessage(session);
        try {
            String adminCred = this.getAdmin();
            String adminEmail = adminCred.split("\\|")[0];
            String adminName = adminCred.split("\\|")[1];

            message.setFrom(new InternetAddress("no-reply@binotify.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
            message.setSubject("[Binotify Premium] New Subscription");

            String text = "<table border='0' cellpadding='0' cellspacing='0' width='100%' style='border-collapse:collapse; padding:0; margin-top:20px;'>"
                    +
                    "<tr>" +
                    "<td align='center'>" +
                    "<table style='border: 0; width: 100%;'>" +
                    "<tr style='border: 0;'>" +
                    "<img src='https://storage.googleapis.com/pr-newsroom-wp/1/2018/11/Spotify_Logo_RGB_Green.png'  width='200px' />"
                    +
                    "</tr>" +
                    "<tr style='border: 0;'>" +
                    "<h2 style='width:250px; margin-top: 10px'>New Subscription Request</h2> " +
                    "</tr>" +
                    "<tr style='border: 0;'>" +
                    "<div style='width:400px;'>Hi " + adminName
                    + ", it looks like there's someone who makes a new subscription request. Don't forget to check them out!</div>"
                    +
                    "</tr>" +
                    "<tr style='border: 0;'>" +
                    "<div style='width:400px margin-top:10px;'><br /><strong>Subscriber ID</strong>: " + subscriber_id
                    + "</div>" +
                    "</tr>" +
                    "<tr style='border: 0;'>" +
                    "<div style='width:400px margin-bottom:10px;'><strong>Creator ID</strong>: " + creator_id + "</div>"
                    +
                    "</tr>" +
                    "<tr style='border: 0;'>" +
                    "<a href='http://localhost:3000/subscription' target='_blank' style='cursor:pointer;'>" +
                    "<button style='width:200px; height:40px; background-color:#1DB954; color:white; border:none; border-radius:5px; margin-top:25px; cursor:pointer;'>Check Now</button>"
                    +
                    "</a>" +
                    "</tr>" +
                    "</table>" +
                    "</td>" +
                    "</tr>" +
                    "</table>";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
