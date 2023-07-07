package cn.edu.bistu.diary.Data;

import android.content.Context;
import android.content.SharedPreferences;

import cn.edu.bistu.diary.Data.Dao.UserDao;
import cn.edu.bistu.diary.Data.Model.User;

public class UserRepository {

    private static volatile UserRepository instance;
    private final static User ANONYMOUS_USER = new User("匿名", null);

    private final UserDao dataSource;

    private User user = ANONYMOUS_USER;
    private boolean loggedIn = false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private UserRepository(UserDao dataSource) {
        this.dataSource = dataSource;
    }

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(new UserDao(new DatabaseHelper(context, "Diary.db", null, 1)));
            instance.pref = context.getSharedPreferences("author", Context.MODE_PRIVATE);
            instance.editor = instance.pref.edit();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean changePassword(String password) {
        if (!isLoggedIn())
            return false;
        user.setPassword(password);
        return dataSource.update(user) == 1;
    }

    public void logout() {
        user = ANONYMOUS_USER;
        loggedIn = false;
        editor.clear();
        editor.commit();
    }

    private void setLoggedInUser(User user) {
        if (user != null) {
            this.user = user;
            this.loggedIn = true;
        }
    }

    public boolean login(String username, String password) {
        User result = dataSource.loginOrRegister(username, password);
        if (result != null) {
            setLoggedInUser(result);
            editor.putString("name", user.getName());
            editor.commit();
            return true;
        }
        return false;
    }

    public void authFromPref() {
        if (isLoggedIn())
            return;
        String name = pref.getString("name", "匿名");
        if (!"匿名".equals(name)) {
            setLoggedInUser(dataSource.findByName(name));
        }
    }
}