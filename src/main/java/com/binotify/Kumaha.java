package com.binotify;

import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService
public class Kumaha {
    @WebMethod
    public String helloWorld(String name) {
        return "Hello" + name;
    }
}
