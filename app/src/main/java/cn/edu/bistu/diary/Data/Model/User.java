package cn.edu.bistu.diary.Data.Model;

public class User {

    private int userId;
    private String name;
    private String password;

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public User(String name, String password) {
        this.password = password;
        this.name = name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}