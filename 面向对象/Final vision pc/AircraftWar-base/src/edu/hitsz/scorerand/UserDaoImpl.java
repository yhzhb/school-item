package edu.hitsz.scorerand;

import java.io.IOException;
import java.util.*;

public class UserDaoImpl implements UserDao{
         private  List<User> users =new LinkedList<>();

         @Override
         public List<User> getAllUsers(){
             return users;
         }

         @Override
         public void addUser(User user){
             users.add(user);
         }

    /*

    @Override
         public void print(){
             System.out.println("**************************************************\n" +
                     "                   得分排行榜             \n" +
                     "**************************************************");

             for(int size = users.size(),i=0;i<size;i++){
                 System.out.println("第"+(i+1)+"名："+users.get(i).getUsername()+","+users.get(i).getUserscore()+","+users.get(i).getAddtime());
             }
             /**
              *ois是输出
              */
            /* for(int size = users.size(),i=0;i<size;i++){
                 System.out.println("第"+(i+1)+"名："+users.get(i).toString());
             }
         }
         */


    /**
     * 根据用户分数冒泡排序用户数据
     */
    @Override
         public void makerand(){
             if(users.size()>1) {
                 for (int size = users.size(), i = 0; i < size-1; i++) {
                     for(int j =0;j<size-1-i;j++) {
                         if (users.get(j + 1).getUserscore() > users.get(j).getUserscore()) {
                             Collections.swap(users, j, j + 1);
                         }
                     }
                 }
             }
         }

         @Override
         public void delete(int num){
              users.remove(users.get(num));
         }
    /**
     * 向文本写入数据
     * @throws IOException
     */
    @Override
    public void dosave() throws IOException {
        Messagemange.WriterTxt(users);
    }

    /**
     * 从文本读入数据录入User
     * @throws IOException
     */
    @Override
    public void doload() throws IOException{
         Messagemange.readText(this);
    }

}
