package com.autonise.myapplication;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

public class Secure_reader {

    static String TAG = "tag:1";

    public static String read(String passwordString, String filename){


        try {
            Map map;
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            map = (HashMap) in.readObject();
            in.close();
            fileIn.close();

            byte salt[] = ((String)map.get("salt")).getBytes("ISO-8859-1");
            byte iv[] = ((String)map.get("iv")).getBytes("ISO-8859-1");
            byte encrypted[] = ((String)map.get("encrypted")).getBytes("ISO-8859-1");
            byte[] decrypted = null;

            //regenerate key from password
            char[] passwordChar = passwordString.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1324, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, "ISO-8859-1");
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
        catch(ClassNotFoundException e)
        {
            Log.d(TAG + "1", e.getMessage());
        }
        return "not_correct";
    }
}
