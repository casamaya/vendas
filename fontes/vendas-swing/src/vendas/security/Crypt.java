/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.security;

/**
 *
 * @author sam
 */
public class Crypt {
    public static String encrypt(String user, String password) throws Exception {
        String sign = user + password;

//        try {
        java.security.MessageDigest md =
                java.security.MessageDigest.getInstance("MD5");
        md.update(sign.getBytes());
        byte[] hash = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0").append(Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        sign = hexString.toString();
//        } catch (Exception nsae) {
//            nsae.printStackTrace();
        //       }
        return sign;
    }
}
