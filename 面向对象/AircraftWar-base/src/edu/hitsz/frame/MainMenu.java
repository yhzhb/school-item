package edu.hitsz.frame;

import edu.hitsz.application.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel  {
    private JPanel Buttonpanel;
    private JButton EasyButton;
    private JButton NormalButton;
    private JButton HardButton;
    private JComboBox comboBox1;
    private JLabel notice;
    private AbstractGame GameLevel;
    private boolean shift=true;
    private String level;


    public MainMenu() {
        comboBox1.setSelectedIndex(0);
        EasyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Buttonpanel.setVisible(false);
                synchronized (Main.obj) {
                    Main.obj.notify();
                    setGameLevel(new EasyGame());
                    level = "Easy";
                    System.out.println("Hello,it's "+level);
                }
            }
        });
        NormalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Buttonpanel.setVisible(false);
                synchronized (Main.obj) {
                    Main.obj.notify();
                    setGameLevel(new NormalGame());
                    level = "Normal";
                    System.out.println("Hello,it's "+level);
                }
            }
        });
        HardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Buttonpanel.setVisible(false);
                synchronized (Main.obj) {
                    Main.obj.notify();
                    setGameLevel(new HardGame());
                    level ="Hard";
                    System.out.println("Hello,it's "+level);
                }
            }
        });
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem()=="开"){
                      System.out.println("音效开");
                }
                else if(comboBox1.getSelectedItem()=="关"){
                    shift=false;
                    System.out.println("音效关");
                }
            }
        });


    }


    public void setGameLevel(AbstractGame game){
        this.GameLevel=game;
    }

    public JPanel getButtonpanel(){

        return Buttonpanel;
    }

    public AbstractGame getGameLevel(){
        return GameLevel;
    }

    public boolean getshift(){
        return  shift;
    }

    public String getLevel(){
        return level;
    }


}
