package com.mySampleApplication.shared.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encode {
    public static String sha(String str) {
        String result = str;

        try {
            MessageDigest m = MessageDigest.getInstance("SHA");
            m.reset();
            m.update(str.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            result = hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result;
    }
}
