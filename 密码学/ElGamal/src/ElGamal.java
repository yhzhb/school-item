import java.awt.*;
import java.math.BigInteger;
import java.util.*;
import java.util.List;

public class ElGamal {

    private static BigInteger p, g; // 大素数和本原根
    private static final BigInteger ONE = BigInteger.ONE;


    /**生成公私钥**/
    public HashMap<String,BigInteger> makeKeyPair(int size){
        HashMap<String, BigInteger> keyPair = new HashMap<>();
        initP(size);
        BigInteger privateKey = getRandom_x(p);
        BigInteger publicKey = g.modPow(privateKey,p);
        keyPair.put("privateKey",privateKey);
        //System.out.println("privateKey:"+ privateKey);
        keyPair.put("publicKey",publicKey);
        System.out.println("publicKey:"+publicKey);
        return keyPair;
    }


    /**签名**/
    public HashMap<BigInteger, List<BigInteger>> encryElGamal(BigInteger g,BigInteger x,BigInteger p,BigInteger m,int cont){
          BigInteger k;

          Random rand = new Random();
          Random random = new Random();
          HashMap<BigInteger, List<BigInteger>> encryMessage = new HashMap<>();
          for(int i=0;i<cont;i++){
              List<BigInteger> c = new ArrayList<>();
              do {
                  int  randNumber = random.nextInt(p.bitLength()/2)+p.bitLength()/2;
                  k =  new BigInteger(randNumber,rand);
              }while (encryMessage.containsKey(k) || !k.gcd(p.subtract(BigInteger.ONE)).equals(ONE));
              BigInteger r = g.modPow(k,p);
              c.add(r);
              //System.out.println(r);
              BigInteger kInverse = k.modInverse(p.subtract(BigInteger.ONE));
              BigInteger temp = m.subtract(x.multiply(r));
             // System.out.println(temp.compareTo(BigInteger.ZERO)>0);
             // BigInteger s;
             // if(temp.compareTo(BigInteger.ZERO)<0) {
             //   s = kInverse.multiply(temp.negate()).mod(p.subtract(ONE));
            //  }else {
               BigInteger   s = kInverse.multiply(temp).mod(p.subtract(ONE));
             // }
              c.add(s);
              //System.out.println(c.size());
              encryMessage.put(k,c);
          }
        return  encryMessage;
    }


    /**验证**/
    public void decryElGamal(BigInteger m,BigInteger p,HashMap<BigInteger, List<BigInteger>> enctry,BigInteger publicKey,BigInteger g){

        for(BigInteger key : enctry.keySet()){
           BigInteger r = enctry.get(key).get(0);
           BigInteger s = enctry.get(key).get(1);


           BigInteger v1 = g.modPow(m,p);
           BigInteger v2 = publicKey.modPow(r,p).multiply(r.modPow(s,p)).mod(p);

           if (v2.equals(v1)){
               System.out.println("当前的k："+key);
               System.out.println("验证成功！");
           }else {
               System.out.println("当前的k："+key);
               System.out.println("验证失败，存在篡改可能！");
           }
        }

    }

    public void initP(int size){
           Random r = new Random();
           BigInteger q;
           while(true){
               q = BigInteger.probablePrime(size,r);
               if(q.isProbablePrime(100)){
                   p = q.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE);
                   if(p.isProbablePrime(100)){
                       break;
                   }
               }
           }

           do {
               if(p.bitLength()>1) {
                   g = BigInteger.probablePrime(p.bitLength() - 1, r);
               }else {
                   g= BigInteger.probablePrime(1,r);
               }
           }while (g.compareTo(p)>1||g.modPow(BigInteger.valueOf(2), p).equals(BigInteger.ONE) || g.modPow(q, p).equals(BigInteger.ONE));
        System.out.println("大素数："+p);
        //System.out.println(q);
        System.out.println("本原根："+g);
    }


    public BigInteger getRandom_x(BigInteger p){
        Random r =new Random();
        BigInteger x;
        if(p.bitLength()>1) {
            return new BigInteger(p.bitLength() - 1, r);
        }
        do {
             x = new BigInteger(1,r);
        }while (x.compareTo(p)>1);

        return x;
    }

    public static BigInteger getG() {
        return g;
    }

    public static BigInteger getP(){
        return  p;
    }

    /**递归遍历获得本源根太慢不采用**/
    public static BigInteger getG(BigInteger p, BigInteger p_MinusOne) {
        BigInteger g = null;

        int i = 2;
        while (g == null){
            for (int x1 = 1; x1 <= Integer.valueOf(p_MinusOne.toString()); x1++) {
                String str1 = String.valueOf(i);
                String str2 = String.valueOf(x1);
                BigInteger tmp1 = new BigInteger(str1);
                BigInteger tmp2 = new BigInteger(str2);
                if (tmp1.modPow(tmp2, p).compareTo(ONE) == 0 && tmp2.compareTo(p_MinusOne) == -1) {
                    break;
                } else if (tmp1.modPow(tmp2, p).compareTo(ONE) == 0 && tmp2.compareTo(p_MinusOne) == 0) {
                    g = tmp1;
                    break;
                }
            }
            i++;
        }

        return g;
    }

    /**只检查Euler(m)因子作幂次，可以更进一步只检查Euler(m)对所有质因子的商
     *例如求任何一个质数x的任何一个原根，一般就是枚举2到x-1，并检验。有一个方便的方法就是，求出x-1所有不同的质因子p1,p2...pm，对于任何2<=a<=x-1,判定a是否为x的原根，只需要检验a^((x-1)/p1),a^((x-1)/p2),...a^((x-1)/pm)这m个数中，是否存在一个数mod x为1，若存在，a不是x的原根，否则就是x的原根。
     * ————————————————*/
    static boolean isOrigin(BigInteger a, BigInteger m) {
        if (a.gcd(m).intValue() != 1) return false;
        BigInteger i = new BigInteger("2");
        while (i.compareTo(m.subtract(BigInteger.ONE)) == -1) {
            if (m.mod(i).intValue() == 0) {
                if (a.modPow(i, m).intValue() == 1)
                    return false;
                while (m.mod(i).intValue() == 0)
                    m = m.divide(i);
            }
            i = i.add(BigInteger.ONE);
        }
        return true;
    }


}
