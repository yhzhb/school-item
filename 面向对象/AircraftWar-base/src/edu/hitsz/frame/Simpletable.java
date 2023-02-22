package edu.hitsz.frame;

import edu.hitsz.application.Main;
import edu.hitsz.scorerand.User;
import edu.hitsz.scorerand.UserDao;
import edu.hitsz.scorerand.UserDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static edu.hitsz.application.Main.obj;

public class Simpletable  {
    private JTable table1;
    private JButton deletebutton;
    private  JPanel Topjpanel;
    private JPanel bottomjpanel;
    private JLabel listLabel;
    private JLabel levelLabel;
    private JScrollPane tablescrollpane;
    private List<User> users;
    private int row;
    private UserDao userDao;
    private DefaultTableModel model;
    public Simpletable() throws IOException {
        levelLabel.setText("难度："+ Main.getMenu().getLevel());
        String[] columnName={"排名","玩家名","得分","记录时间"};
        userDao= Main.getGame().getUserDao();
        if(userDao!=null) {
            userDao.makerand();
            users = userDao.getAllUsers();
            String[][] Userdata = new String[users.size()][4];
            for (int size = users.size(), i = 0; i < size; i++) {
                Userdata[i][0] = String.valueOf(i + 1);
                Userdata[i][1] = users.get(i).getUsername();
                Userdata[i][2] = String.valueOf(users.get(i).getUserscore());
                Userdata[i][3] = users.get(i).getAddtime();
            }

            userDao.dosave();

            model = new DefaultTableModel(Userdata, columnName) {
                @Override
                public boolean isCellEditable(int row, int coi) {
                    return false;
                }
            };

            table1.setModel(model);
            tablescrollpane.setViewportView(table1);
        }
        deletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                row = table1.getSelectedRow();
                delete box = new delete();
                if(row!=-1) {
                    box.createdelete();
                }
                else {
                     JOptionPane.showMessageDialog(null, "请用鼠标点击选择一个玩家！","提示",JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
    }


    public JPanel  getTopjpanel(){
        return Topjpanel;
    }


    public int getRow() {
        return row;
    }

    public UserDao getUserDao(){
        return userDao;
    }

    public DefaultTableModel getModel(){
        return model;
    }
}
