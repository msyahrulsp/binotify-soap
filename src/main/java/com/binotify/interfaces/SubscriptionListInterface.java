package com.binotify.interfaces;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@WebService
public interface SubscriptionListInterface {
    @Resource
    @WebMethod
    @XmlElementWrapper
    @XmlElement(name = "return")
    String subscriptionList();
}
