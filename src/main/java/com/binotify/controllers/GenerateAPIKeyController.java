package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.GenerateAPIKeyInterface;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.security.SecureRandom;
import java.util.Base64;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface = "com.binotify.interfaces.GenerateAPIKeyInterface")
public class GenerateAPIKeyController extends Database implements GenerateAPIKeyInterface {
    @Resource WebServiceContext wsContext;
    @WebMethod
    public String generateAPIKey(@WebParam(name = "email") String email) {
        SecureRandom randomizer = new SecureRandom();
        byte bytes[] = new byte[32];
        randomizer.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);

        String query = "INSERT INTO `api-key` (`id`, `email`, `api_key`) VALUES (NULL, '" + email + "', '" + token + "')";

        try {
            int res = executeUpdate(query);
            String description = "Generate API Key untuk user " + email;
            String endpoint = "/generate-api-key";
            if (res != 0) {
                insertLog(wsContext, description, endpoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return token;
    }

//    public static void main(String[] args) {
//        GenerateAPIKeyController ctr = new GenerateAPIKeyController();
//        System.out.println("api key" + ctr.generateAPIKey("test@gmail.com"));
//    }
}
