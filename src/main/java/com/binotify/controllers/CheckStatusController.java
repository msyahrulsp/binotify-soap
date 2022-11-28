package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.CheckStatusInterface;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@WebService(endpointInterface = "com.binotify.interfaces.CheckStatusInterface")
public class CheckStatusController extends Database implements CheckStatusInterface {
    @Resource WebServiceContext wsContext;
    @WebMethod
    public boolean getStatus(@WebParam(name = "creator_id") int creator_id, @WebParam(name = "subscriber_id") int subscriber_id) {
        if (verifyAPIKey(wsContext)) {
            String query = "select * from subscription where creator_id = " + creator_id + " and subscriber_id = " + subscriber_id;
            String status = "";
            try {
                ResultSet res = this.executeQuery(query);
                List<Map<String, Object>> data = getFormattedRes(res);
                if (data != null) {
                    status = (String) data.get(0).get("status");
                    insertLog(wsContext, "Mengecek status subscription", "/subscription-status");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
            return status.equals("ACCEPTED");
        } else {
            return false;
        }
    }

//    public static void main(String[] args) {
//        CheckStatusController ctr = new CheckStatusController();
//        System.out.println(ctr.getStatus(1,1));
//    }
}

