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
import java.util.Map;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException ;

public class store_securely {

	public static void main(String[] args) throws ClassNotFoundException, InvalidAlgorithmParameterException, BadPaddingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {

		Map map = new HashMap();

		SecureRandom random = new SecureRandom();
		byte salt[] = new byte[256];
		random.nextBytes(salt);
		String passwordString = "changeit";
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
		byte[] encrypted = cipher.doFinal("plainTextBytes".getBytes());
		String salt_str = new String(salt, "ISO-8859-1");
		String iv_str = new String(iv, "ISO-8859-1");
		String encrypted_str = new String(encrypted, "ISO-8859-1");
		map.put("salt", salt_str); 	
		map.put("iv", iv_str);
		map.put("encrypted", encrypted_str);
		FileOutputStream fileOut = new FileOutputStream("map.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(map);
		out.close();
		fileOut.close();
	}


}