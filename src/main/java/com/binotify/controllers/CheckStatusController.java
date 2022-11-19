package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.CheckStatusInterface;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@WebService(endpointInterface = "com.binotify.interfaces.CheckStatusInterface")
public class CheckStatusController extends Database implements CheckStatusInterface {
    @WebMethod
    public boolean getStatus(int creator_id, int subscriber_id) {
        String query = "select * from subscription where creator_id = " + creator_id + " and subscriber_id = " + subscriber_id;
        String status = null;
        try {
            ResultSet res = this.executeQuery(query);
            List<Map<String, Object>> data = getFormattedRes(res);status = (String) data.get(0).get("status");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return status == "ACCEPTED";
    }

//    public static void main(String[] args) {
//        CheckStatusController ctr = new CheckStatusController();
//        System.out.println(ctr.getStatus(1,2));
//    }
}

