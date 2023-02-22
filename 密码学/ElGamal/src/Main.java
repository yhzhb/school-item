import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {
    private  static final SHAUtil shaUtil=new SHAUtil();
    private  static final ElGamal elGamal = new ElGamal();
    private static final String private_key = "privateKey";
    private static HashMap <String,BigInteger> key = new HashMap<>();
    private static HashMap <BigInteger, List<BigInteger>> enctry = new HashMap<>();
    private static final int size = 1024;

    public static void main(String[] args) {

        String str;
        String cha;
        Scanner input;
        do {
            System.out.println("请输入需要加密的明文:");
            input = new Scanner(System.in);
            str = input.nextLine();
        } while (str.length()==0);
       // System.out.println(str);
        String SHA_str = shaUtil.SHA256(str);
        // System.out.println(SHA_str);
        // Integer x = Integer.parseInt("deae9becc8b36d5d91dad4c67adf8b0417893ec98fd60270215df15480550aaa",16);

        BigInteger x = shaUtil.getHm(SHA_str);
        System.out.println("生成的明文的Hash值："+x);

        while (true) {
            System.out.println("1.密钥生成  2.进行签名  3.进行验证 4.退出");
            String cho1 = input.nextLine();
            if(cho1.length()==0){
                cho1 = "1";
            }
            switch (Integer.parseInt(cho1)) {

                case 2: {
                    enctry = elGamal.encryElGamal(ElGamal.getG(), key.get(private_key), ElGamal.getP(), x, 2);
                    int cnt = 0;
                    for (BigInteger key : enctry.keySet()) {
                        System.out.println("k"+(cnt+1)+":"+ key);
                        for (int i = 0; i < enctry.get(key).size(); i++) {
                            if(i==0) {
                                System.out.println("签名信息r:" + enctry.get(key).get(i));
                            }else {
                                System.out.println("签名信息s:"+ enctry.get(key).get(i));
                            }
                        }
                        cnt++;
                        System.out.println("*********************************");
                    }
                    break;
                }
                case 3: {
                    System.out.println("开始模拟传输数据");
                    System.out.println("是否发动篡改攻击（Y/N?)");
                    cho1 = input.nextLine();
                    if(cho1.equals("Y")){
                        do {
                            System.out.println("请输入篡改的明文:");
                            cha= input.nextLine();
                        }while (cha.length()==0);
                       // System.out.println(3);
                        elGamal.decryElGamal(shaUtil.getHm(cha),ElGamal.getP(),enctry,key.get("publicKey"),ElGamal.getG());
                        System.out.println("原信息:"+str+"    对应的哈希值："+shaUtil.SHA256(str));
                        System.out.println("篡改的信息："+cha+ "     对应的哈希值："+shaUtil.SHA256(cha));
                    }else {
                        elGamal.decryElGamal(x,ElGamal.getP(),enctry,key.get("publicKey"),ElGamal.getG());
                    }

                    break;
                }
                case 4: {
                    input.close();
                    exit(0);
                }
                default:{
                        key = elGamal.makeKeyPair(512);
                        System.out.println(private_key + ":" + key.get(private_key));
                        break;
                }
            }
            //System.out.println(str.hashCode());
            //System.out.println(SHA_str.hashCode());
            //BigInteger m = new BigInteger(String.valueOf(real_s.hashCode()));
            // System.out.println(m);
            // System.out.println(m.add(BigInteger.ONE));
        }

    }




}
