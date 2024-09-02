package com.example.jwttokendemo.token;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.Test;


import javax.crypto.SecretKey;

public class JwtSecretMakerTest {

    @Test
    public void generateSecretKey(){
        SecretKey key = Jwts.SIG.HS512.key().build();
       String secretKey =  DatatypeConverter.printHexBinary(key.getEncoded());

        System.out.printf("\n Secret key: [%s]\n" ,secretKey);
    }
}
