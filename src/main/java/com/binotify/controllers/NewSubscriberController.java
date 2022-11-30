package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.NewSubscriberInterface;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import java.sql.ResultSet;

@WebService(endpointInterface = "com.binotify.interfaces.NewSubscriberInterface")
public class NewSubscriberController extends Database implements NewSubscriberInterface {
    @Resource WebServiceContext wsContext;
    @WebMethod
    public boolean newSubscribe(@WebParam(name = "creator_id") int creator_id, @WebParam(name = "subscriber_id") int subscriber_id) {
        if (verifyAPIKey(wsContext)) {
            String query = "insert into subscription (creator_id, subscriber_id, status) values ('" + creator_id + "', '" + subscriber_id + "', 'PENDING')";
            try {
                int res = this.executeUpdate(query);
                if (res != 0) {
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

}
