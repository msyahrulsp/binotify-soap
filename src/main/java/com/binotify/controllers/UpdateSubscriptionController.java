package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.UpdateSubscriptionInterface;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface = "com.binotify.interfaces.UpdateSubscriptionInterface")
public class UpdateSubscriptionController extends Database implements UpdateSubscriptionInterface {
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
