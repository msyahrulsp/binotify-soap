package com.binotify.controllers;

import com.binotify.interfaces.CheckStatusInterface;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(endpointInterface = "com.binotify.interfaces.CheckStatusInterface")
public class CheckStatusController implements CheckStatusInterface {
    
    @WebMethod
    public Boolean getStatus(Integer id) {
        return true;
    }
}
