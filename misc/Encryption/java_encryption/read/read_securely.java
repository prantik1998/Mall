import java.io.PrintWriter;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.Map;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException ;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class read_securely {

	public static void main(String[] args) throws ClassNotFoundException, InvalidAlgorithmParameterException, BadPaddingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {

		Map map = new HashMap();
		FileInputStream fileIn = new FileInputStream("map.ser");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		map = (HashMap) in.readObject();
		in.close();
		fileIn.close();

		byte salt[] = ((String)map.get("salt")).getBytes("ISO-8859-1");
		byte iv[] = ((String)map.get("iv")).getBytes("ISO-8859-1");
		byte encrypted[] = ((String)map.get("encrypted")).getBytes("ISO-8859-1");
		byte[] decrypted = null;
		String passwordString = "changeit";
 
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
		String decrypted_str = new String(decrypted, "ISO-8859-1");
	}


}