package cn.edu.bistu.diary.Data.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.edu.bistu.diary.Data.DatabaseHelper;
import cn.edu.bistu.diary.Data.Model.User;

@SuppressLint("Range")
public class UserDao {
    private final DatabaseHelper dbHelper;

    public UserDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    private User getByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();     //通过dbHelper获得可读文件
        Cursor cursor = db.query("User", new String[]{"id", "password"},
                "name=?", new String[]{name + ""}, null, null, null);
        User user = null;
        if (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            user = new User(id, name);
            user.setPassword(password);
        }
        cursor.close();
        return user;
    }

    public User findByName(String name) {
        User user = getByName(name);
        if (user != null)
            user.setPassword(null);
        return user;
    }

    public int update(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", user.getPassword());
        return db.update("User", values, "id=?", new String[]{user.getUserId() + ""});
    }

    private void register(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", username);
        values.put("password", password);
        db.insert("User", null, values);
    }

    public User loginOrRegister(String username, String password) {
        try {
            User user = getByName(username);
            if (user != null && !user.getPassword().equals(password)) {
                return null;
            }
            if (user == null) {
                register(username, password);
                user = getByName(username);
            }
            user.setPassword(null);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}