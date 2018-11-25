package com.autonise.myapplication;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Secure_writer {

    static String TAG = "tag:1";

    public static void write(String message, String passwordString, String filename){


        try {
            Map map = new HashMap();

            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);
            char[] passwordChar = passwordString.toCharArray(); //Turn password into char[] array
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1324, 256); //1324 iterations
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(message.getBytes());
            String salt_str = new String(salt, "ISO-8859-1");
            String iv_str = new String(iv, "ISO-8859-1");
            String encrypted_str = new String(encrypted, "ISO-8859-1");
            map.put("salt", salt_str);
            map.put("iv", iv_str);
            map.put("encrypted", encrypted_str);
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(map);
            out.close();
            fileOut.close();
        }
        catch(InvalidAlgorithmParameterException e)
        {
            Log.d(TAG+"1", e.getMessage());
        }
        catch(BadPaddingException e)
        {
            Log.d(TAG+"1", e.getMessage());
        }
        catch(IOException e) {
            Log.d(TAG + "1", e.getMessage());
        }
        catch(NoSuchAlgorithmException e)
        {
            Log.d(TAG + "1", e.getMessage());
        }
        catch(InvalidKeySpecException e)
        {
            Log.d(TAG + "1", e.getMessage());
        }
        catch(NoSuchPaddingException e)
        {
            Log.d(TAG + "1", e.getMessage());
        }
        catch(InvalidKeyException e)
        {
            Log.d(TAG + "1", e.getMessage());
        }
        catch(IllegalBlockSizeException e)
        {
            Log.d(TAG + "1", e.getMessage());
        }
    }
}
