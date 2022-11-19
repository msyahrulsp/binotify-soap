package com.binotify.interfaces;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@WebService
public interface CheckStatusInterface {
    @WebMethod
    @XmlElementWrapper
    @XmlElement(name = "return")
    boolean getStatus(int creator_id, int subscriber_id);
}
