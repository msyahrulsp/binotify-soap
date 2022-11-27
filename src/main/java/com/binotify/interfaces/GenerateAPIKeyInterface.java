package com.binotify.interfaces;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@WebService
public interface GenerateAPIKeyInterface {
    @WebMethod
    @XmlElementWrapper
    @XmlElement(name = "return")
    String generateAPIKey(@WebParam(name = "email") String email);
}
