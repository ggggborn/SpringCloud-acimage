package com.acimage.community.utils;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class RsaUtils {
    private static final String privateKey;
    private static final String publicKey;

    static {
        RSA rsa = new RSA();
        privateKey=rsa.getPrivateKeyBase64();
        publicKey =rsa.getPublicKeyBase64();
    }
    public static String decrypt(String privateKeyBase64,String encryptBase64){
        RSA rsa=new RSA(privateKeyBase64,null);
        return rsa.decryptStr(encryptBase64, KeyType.PrivateKey);
    }

    public static String getPrivateKey(){
        return privateKey;
    }
    public static String getPublicKey(){
        return publicKey;
    }
}
