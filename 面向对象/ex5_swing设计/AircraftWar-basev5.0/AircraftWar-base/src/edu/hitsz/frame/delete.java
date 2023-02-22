package edu.hitsz.frame;

import edu.hitsz.application.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static edu.hitsz.application.Main.*;

public class delete extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonEsc;
    private JLabel notice;
    private Boolean flag=false;
    public static  Object obj=new Object();
    public delete() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonEsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    private void onOK() {
        if (Main.getSimpletable().getRow()!= -1 ) {
            Main.getSimpletable().getModel().removeRow(Main.getSimpletable().getRow());
            Main.getSimpletable().getUserDao().delete(Main.getSimpletable().getRow());
            try {
                Main.getSimpletable().getUserDao().dosave();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
            dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public  void createdelete() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        delete dialog = new delete();
        dialog.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2+WINDOW_WIDTH/4, WINDOW_HEIGHT/2,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        dialog.pack();

        dialog.setVisible(true);
    }

}
