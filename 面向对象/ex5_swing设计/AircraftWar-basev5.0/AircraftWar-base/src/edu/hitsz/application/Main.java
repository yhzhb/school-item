package edu.hitsz.application;

import edu.hitsz.frame.MainMenu;
import edu.hitsz.frame.Simpletable;
import edu.hitsz.frame.getName;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static Object obj =new Object();

    private static AbstractGame game;
    private static Simpletable simpletable;
    private static MainMenu menu;


    public static void main(String[] args)  {


        System.out.println("Hello Aircraft War");



        // 获得屏幕的分辨率，初始化 Frame

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * 菜单界面
         */
             menu = new MainMenu();
            frame.add(menu.getButtonpanel());
            frame.setVisible(true);


        /**
         * 游戏界面
         */
        synchronized (obj) {
         try {
             obj.wait();
             frame.remove(menu.getButtonpanel());
             game =  menu.getGameLevel();
             frame.add(game);
             frame.setVisible(true);
             game.action();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

         /**
         * 分数榜
         */
        synchronized (obj) {
            try {
                obj.wait();
                frame.remove(menu.getGameLevel());
                simpletable=new Simpletable();
                frame.add(simpletable.getTopjpanel());
                frame.setVisible(true);
                getName getname = new getName();
                getname.creategetNameBox();
                frame.remove(simpletable.getTopjpanel());
                simpletable=new Simpletable();
                frame.add(simpletable.getTopjpanel());
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public static AbstractGame getGame(){
        return game;
    }

    public static MainMenu getMenu(){
        return menu;
    }

    public static Simpletable getSimpletable(){
        return simpletable;
    }



}
