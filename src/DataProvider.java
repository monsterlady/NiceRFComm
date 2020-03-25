import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DataProvider {
    private SecretKey key;
    private long unixTime;
//    T: Tank
//    S: Soldier
//    A: Artillery
//    I: Infantry
//    W: Walker
//    L: Laser
//    C: Clear
    public enum Obj {
        TANK(1,"T"),
        SOLDIER(2,"S"),
        ARTILLERY(3,"A"),
        INFANTRY(4,"I"),
        WALKER(5,"W"),
        LASER(6,"L"),
        CLEAR(7,"C");
        private int index;
        private String name;
        private Obj(int index,String name){
            this.index = index;
            this.name = name;
        }

        public static String getNameByIndex(int index){
            for (Obj c : Obj.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }

    public int getIndex() {
        return index;
    }
}

    public static void main(String[] args) {
        String test = new DataProvider().responseScan();
        System.out.println( test);
        System.out.println(encrypt(test));
        System.out.println(decrypt(encrypt(test)));
    }

    public DataProvider() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES/CBC/PKCS5PADDING");
            generator.init(256);
            this.key = generator.generateKey();
        } catch (NoSuchAlgorithmException n){

        }
    }

    public String filterCommand(String str){
        String output = "";
        switch (str){
            case "1":
                output = encrypt(CommandData.ATTACK);
                break;
            case "2":
                output = encrypt(CommandData.RETREAT);
                break;
            case "3":
                output = encrypt(CommandData.SCANAREA);
                break;
            default:
                output = "MSG WRONG";
                break;
        }
        return output;
    }

    public String convertCommand(String str){
        String output = "";
        str = decrypt(str);
        switch (str){
            case CommandData.ATTACK:
                output = CommandData.ATTACK;
                break;
            case CommandData.RETREAT:
                output = CommandData.RETREAT;
                break;
            case CommandData.SCANAREA:
                output = CommandData.SCANAREA;
                break;
            default:
                output = "MSG WRONG";
                break;
        }
        return output;
    }

    public static final String encrypt(String plainText) {
        Key secretKey = getKey("BROPROGRAMMER");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] p = plainText.getBytes("UTF-8");
            byte[] result = cipher.doFinal(p);
            BASE64Encoder encoder = new BASE64Encoder();
            String encoded = encoder.encode(result);
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String decrypt(String cipherText) {
        Key secretKey = getKey("BROPROGRAMMER");
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] c = decoder.decodeBuffer(cipherText);
            byte[] result = cipher.doFinal(c);
            String plainText = new String(result, "UTF-8");
            return plainText;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public String probType(String command)
    {
        String output = "";
        switch (command){
            case CommandData.ATTACK:
                output = responseAttack();
                break;
            case CommandData.RETREAT:
                output = responseRetreat();
                break;
            case CommandData.SCANAREA:
                output = responseScan();
                break;
            default:
                output = "MSG WRONG";
                break;
        }
        return output;
    }

    public String responseScan(){
        //3 1535334221 x28.543 y97.765 T2
        unixTime = System.currentTimeMillis() / 1000L;
        double x = Math.round((Math.random() * 100));
        double y = Math.round((Math.random() * 100));
        int num = (int)(Math.random() * 10);
        int index = (int)(Math.random() * Obj.values().length);
        return "3 " + unixTime + " x" + x + " y" + y + " " + Obj.getNameByIndex(index) + num;
    }

    private String responseRetreat() {
        unixTime = System.currentTimeMillis() / 1000L;
        double x = Math.round((Math.random() * 100));
        double y = Math.round((Math.random() * 100));
//        int num = (int)(Math.random() * 10);
//        int index = (int)(Math.random() * Obj.values().length);
        return "2 " + unixTime + " x" + x + " y" + y + " "+ " RETREAT";
    }

    private String responseAttack() {
        unixTime = System.currentTimeMillis() / 1000L;
        double x = Math.round((Math.random() * 100));
        double y = Math.round((Math.random() * 100));
        int num = (int)(Math.random() * 10);
        int index = (int)(Math.random() * Obj.values().length);
        return "1 " + unixTime + " x" + x + " y" + y + " " + Obj.getNameByIndex(index) + num;
    }

    private static Key getKey(String keySeed) {
        if (keySeed == null) {
            keySeed = System.getenv("AES_SYS_KEY");
        }
        if (keySeed == null) {
            keySeed = System.getProperty("AES_SYS_KEY");
        }
        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = "abcd1234!@#$";// 默认种子
        }
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
