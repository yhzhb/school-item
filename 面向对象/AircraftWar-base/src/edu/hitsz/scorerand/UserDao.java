package edu.hitsz.scorerand;

import java.io.IOException;
import java.util.List;

public interface UserDao {
    List<User>  getAllUsers();
    void addUser(User user);
    void makerand();
    //void print();
    void delete(int num);
    void dosave() throws IOException;
    void doload() throws IOException;
}
