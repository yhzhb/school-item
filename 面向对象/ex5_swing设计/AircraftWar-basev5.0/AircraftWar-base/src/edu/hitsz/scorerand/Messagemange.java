package edu.hitsz.scorerand;

import edu.hitsz.application.Main;
import edu.hitsz.frame.MainMenu;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * 文本处理
 */
public class Messagemange {
    /**
     * 写文本
     * @param userList
     * @throws IOException
     */
    public static void WriterTxt(List<User> userList) throws IOException {
        String pathname = "src/"+ Main.getMenu().getLevel() +".txt";
        File f = new File(pathname);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        for(User user:userList) {
            String name = user.getUsername();
            String score =String.valueOf(user.getUserscore());
            String time  = user.getAddtime();
            bw.write(new String(name.getBytes(), "utf-8")+" "); //解决乱码
            bw.write(new String(score.getBytes(StandardCharsets.UTF_8))+" ");
            bw.write(new String(time.getBytes(StandardCharsets.UTF_8))+" ");
            bw.newLine();
        }
            bw.flush();
            try {
                fw.flush();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    /**
     * 写文本
     * @param userDao
     * @throws IOException
     */
    public static void readText(UserDao userDao) throws IOException {
         String pathname = "src/"+ Main.getMenu().getLevel() +".txt";
         File f = new File(pathname);
         if(f.isFile()&& f.exists()) {
             Scanner s = new Scanner(f);
             while (s.hasNext()) {
                     String name = s.next();
                     int score = Integer.parseInt(s.next());
                     String timeD = s.next();
                     String timeH = s.next();
                     String time = (timeD + " " + timeH);
                     User newuser = new User(name, score, time);
                     userDao.addUser(newuser);
             }
             s.close();
         }

    }

}
