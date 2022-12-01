package com.binotify.interfaces;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@WebService
public interface UpdateSubscriptionInterface {
    @Resource
    @WebMethod
    @XmlElementWrapper
    @XmlElement(name = "return")
    boolean updateSubscription(@WebParam(name = "creator_id") int creator_id,
            @WebParam(name = "subscriber_id") int subscriber_id,
            @WebParam(name = "status") String status);
}
