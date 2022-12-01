package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.UpdateSubscriptionInterface;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebService(endpointInterface = "com.binotify.interfaces.UpdateSubscriptionInterface")
public class UpdateSubscriptionController extends Database implements UpdateSubscriptionInterface {
    private String phpCallbackURL = System.getenv("PHP_CALLBACK_URL");
    @Resource
    WebServiceContext wsContext;

    @WebMethod
    public boolean updateSubscription(@WebParam(name = "creator_id") int creator_id,
            @WebParam(name = "subscriber_id") int subscriber_id,
            @WebParam(name = "status") String status) {
        if (verifyAPIKey(wsContext)) {
            String query = "update subscription set status = '" + status + "' where creator_id = " + creator_id
                    + " and subscriber_id = " + subscriber_id;
            try {
                int res = this.executeUpdate(query);
                if (res != 0) {
                    insertLog(wsContext, "Mengupdate status subscription", "/subscription-update");
                    boolean callbackStatus = this.phpCallback(phpCallbackURL, creator_id, subscriber_id, status);
                    if (callbackStatus) {
                        insertLog(wsContext, "Mengupdate status subscription pada Binotify App", "/subscription-update");
                        return true;
                    }
                    return false;
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

    private boolean phpCallback(String url, int creatorId, int subscriberId, String status) {
        try {
            URL phpEndpoint = new URL(url);
            HttpURLConnection con = (HttpURLConnection)phpEndpoint.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = "{\"creator_id\": " + creatorId + ", \"subscriber_id\": " + subscriberId + ", \"status\": \"" + status + "\"}";
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                return false;
            }
            try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }

    }
}
