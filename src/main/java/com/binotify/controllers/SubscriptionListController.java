package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.SubscriptionListInterface;
import com.google.gson.Gson;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@WebService(endpointInterface = "com.binotify.interfaces.SubscriptionListInterface")
public class SubscriptionListController extends Database implements SubscriptionListInterface {
    @Resource
    WebServiceContext wsContext;

    @WebMethod
    public String subscriptionList() {
        if (verifyAPIKey(wsContext)) {
            String query = "select * from subscription where status = 'PENDING'";
            try {
                ResultSet res = this.executeQuery(query);
                List<Map<String, Object>> data = getFormattedRes(res);
                if (data.size() > 0) {
                    insertLog(wsContext, "Mendapatkan data subscription", "/subscription-list");
                    return new Gson().toJson(data);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                return null;
            }
        } else {
            return null;
        }
    }
}
