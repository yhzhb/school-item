package edu.hitsz.scorerand;


import java.io.Serializable;

/**
 * 数值对象用户User类
 */
public class User implements  Serializable{
    private int    userscore;
    private String username;
    private String addtime;

    public User(String username, int userscore,String addtime){
        this.username=username;
        this.userscore=userscore;
        this.addtime = addtime;
    }

    public int getUserscore(){
        return userscore;
    }

    public String getUsername() {
        return username;
    }

    public String getAddtime() {
        return addtime;
    }

    @Override
    public String toString(){//ois序列化输出使用
        return username+","+userscore+","+addtime;
    }

}
