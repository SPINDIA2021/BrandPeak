package com.spindiabrand.crypto;

/**
 * Created by Rani on 29/1/18.
 */
public interface Encryption {
    String encrypt(String var1, String var2) throws Exception;

    String decrypt(String var1, String var2) throws Exception;
}
