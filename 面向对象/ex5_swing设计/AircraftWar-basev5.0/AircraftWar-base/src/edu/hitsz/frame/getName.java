package edu.hitsz.frame;

import edu.hitsz.application.AbstractGame;
import edu.hitsz.application.Main;
import edu.hitsz.scorerand.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

public class getName extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JLabel noticelabel;
    private JPanel TopJanpanel;
    private JPanel Buttonpanel;
    private JPanel Textpanel;
    private String Username;

    public getName() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        noticelabel.setText("游戏结束，你的得分为"+ Main.getGame().getScore()+"请输入名字记录得分");

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void onOK()  {
        // 在此处添加您的代码


        if(textField1.getText().isEmpty()||textField1.getText().isBlank()){
            Main.getGame().getUserDao().addUser(new User("null", Main.getGame().getScore(), Main.getGame().getAddtime()));
        }
        else {
            Main.getGame().getUserDao().addUser(new User(textField1.getText(), Main.getGame().getScore(), Main.getGame().getAddtime()));
        }
        System.out.println(textField1.getText());

        System.out.println(Main.getGame().getUserDao().getAllUsers().size());
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public  void creategetNameBox() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        getName dialog = new getName();
        dialog.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2+WINDOW_WIDTH/4, WINDOW_HEIGHT/2,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.pack();
        dialog.setVisible(true);
    }
}
