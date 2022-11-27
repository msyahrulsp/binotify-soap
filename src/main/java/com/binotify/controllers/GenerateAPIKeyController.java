package com.binotify.controllers;

import com.binotify.database.Database;
import com.binotify.interfaces.GenerateAPIKeyInterface;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.security.SecureRandom;
import java.util.Base64;

@WebService(endpointInterface = "com.binotify.interfaces.GenerateAPIKeyInterface")
public class GenerateAPIKeyController extends Database implements GenerateAPIKeyInterface {
    @WebMethod
    public String generateAPIKey(@WebParam(name = "email") String email) {
        SecureRandom randomizer = new SecureRandom();
        byte bytes[] = new byte[32];
        randomizer.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);

        String query = "INSERT INTO `api-key` (`id`, `email`, `api_key`) VALUES (NULL, '" + email + "', '" + token + "')";

        try {
            int res = this.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return token;
    }

//    public static void main(String[] args) {
//        GenerateAPIKeyController ctr = new GenerateAPIKeyController();
//        System.out.println(ctr.generateAPIKey("test@gmail.com"));
//    }
}
