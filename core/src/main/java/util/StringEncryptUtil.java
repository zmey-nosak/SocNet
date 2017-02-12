package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Echetik on 11.02.2017.
 */
public interface StringEncryptUtil {
    String ALGORITHM = "MD5";

    static String encrypt(String s) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
        byte[] bs;
        messageDigest.reset();
        bs=messageDigest.digest(s.getBytes());
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<bs.length;i++){
            String hexVal=Integer.toHexString(0xFF & bs[i]);
            if(hexVal.length()==1){
                stringBuilder.append("0");
            }
            stringBuilder.append(hexVal);
        }
        return stringBuilder.toString();
    }
}
