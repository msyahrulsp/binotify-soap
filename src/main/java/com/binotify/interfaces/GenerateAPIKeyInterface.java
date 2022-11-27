package com.binotify.interfaces;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.ws.WebServiceContext;

@WebService
public interface GenerateAPIKeyInterface {
    @Resource
    @WebMethod
    @XmlElementWrapper
    @XmlElement(name = "return")
    String generateAPIKey(@WebParam(name = "email") String email);
}
