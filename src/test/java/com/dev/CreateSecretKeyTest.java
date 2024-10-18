package com.dev;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class CreateSecretKeyTest {
    @Test
    public void createSecreteKey(){
        SecretKey key = Jwts.SIG.HS512.key().build();

        //this will convert it into String
        String strKey = DatatypeConverter.printHexBinary(key.getEncoded());
        System.out.println("Key"+strKey);
    }
}
