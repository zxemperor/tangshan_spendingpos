package vdpos.com.tcps.basicplug.util.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Des3_ecb {
    public static void main(String[] args) throws Exception {
        String tKey = "005964B2744A4C339B5FEB366790C45B";
        String data = "AA3000810123452ED70C38908BC00A16000004033002704107150711560543000000000004331100010000110000059BB";

//		System.out.println(ECB_TripDESencrypt(tKey, data));
//		System.out.println(ConversionUtil.completeF(data));
//		System.out.println(AppDes.ECB_TripDESencrypt(tKey, ConversionUtil.completeF(data)));
    }

    public static String ECB_TripDEDESencrypt(String key, String sour) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        System.out.println("ECB_TripDEDESencryptKEY:"+key);
        String res = "";
        Cipher c1 = Cipher.getInstance("DES/ECB/NoPadding");
        for (int i = 0; i < sour.length() / 16; i++) {
            SecretKey deskey = new SecretKeySpec(hexstr2byte(key.substring(0, 16)), "DES");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            String tmp = byte2hex(c1.doFinal(hexstr2byte(sour.substring(i * 16, i * 16 + 16)))).toUpperCase();
            System.out.println("DES 临时值"+tmp);
            res += tmp;
        }
        return res.toUpperCase();
    }


    public static String ECB_TripENDESencrypt(String key, String sour) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        String res = "";
        Cipher c1 = Cipher.getInstance("DES/ECB/NoPadding");
        int lastlen = sour.length() / 16;
        for (int i = 0; i < lastlen; i++) {
            SecretKey deskey = new SecretKeySpec(hexstr2byte(key.substring(0, 16)), "DES");
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            String tmp = byte2hex(c1.doFinal(hexstr2byte(sour.substring(i * 16, i * 16 + 16)))).toUpperCase();
//			deskey = new SecretKeySpec(hexstr2byte(key.substring(16)), "DES");
//	        c1.init(Cipher.DECRYPT_MODE, deskey);
//	        tmp = byte2hex(c1.doFinal(hexstr2byte(tmp)));
//	        deskey = new SecretKeySpec(hexstr2byte(key.substring(0, 16)), "DES");
//	        c1.init(Cipher.ENCRYPT_MODE, deskey);
//	        tmp = byte2hex(c1.doFinal(hexstr2byte(tmp))).toUpperCase();
            res += tmp;
        }
        return res.toUpperCase();
    }

    public static byte[] hexstr2byte(String inData) {
        byte[] temp = new byte[inData.length() / 2];
        for (int i = 0; i < inData.length() / 2; i++) {
            temp[i] = (byte) Integer.parseInt(inData.substring(i * 2, i * 2 + 2), 16);
        }
        return temp;
    }

    public static String byte2hex(byte[] readdat) {
        StringBuffer sb = new StringBuffer();
        String t = "";
        for (int i = 0; i < readdat.length; i++) {
            t = Integer.toHexString(readdat[i]);
            if (t.length() == 1) {
                sb.append("0" + t);
            } else if (t.length() > 2) {
                sb.append(t.substring(t.length() - 2, t.length()));
            } else {
                sb.append(t);
            }
        }
        return sb.toString();
    }
}
