package edu.hitsz.scorerand;

import java.io.*;
import java.util.List;

/**
 * 序列化方式写入写出，乱码，读写覆盖
 */

public class oisMessage {
    public static void WriterTxt(List<User> userList) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("d:/flywar.txt")));
        for(User user:userList) {
            oos.writeObject(user);
        }
        System.out.println("flywar序列化完成");
        oos.close();
    }

    public static void readText(UserDao userDao) throws IOException, ClassNotFoundException {
        FileInputStream is = new FileInputStream("d:/flyPig.txt");
        ObjectInputStream ois = new ObjectInputStream(is);
        while (is.available()>0) {
            User person = (User) ois.readObject();
            System.out.println(is.available());
            userDao.addUser(person);
        }
        ois.close();
    }
}
