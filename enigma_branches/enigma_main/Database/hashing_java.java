import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String inputString = "python";
            byte[] encodedString = inputString.getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(encodedString);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String hashResult = hexString.toString();
            System.out.println(hashResult);// 11a4a60b518bf24989d481468076e5d5982884626aed9faeb35b8576fcd223e1
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
