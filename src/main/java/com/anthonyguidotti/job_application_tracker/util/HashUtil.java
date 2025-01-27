package com.anthonyguidotti.job_application_tracker.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
    private static final String HASH_ALGORITHM = "MD5";

    private HashUtil() {

    }

    public static String md5Hash(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(input);
            byte[] hashByteArray = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : hashByteArray) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
