package cn.edu.bistu.diary.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//对数据库进行创建，包括记事录的id、文本标题、作者信息、内容信息、图片和数据时间
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_DIARY = "create table Diary("
            + "id integer primary key autoincrement,"
            + "title text not null,"
            + "author text,"
            + "content text,"
            + "imagePath text,"
            + "date datetime not null default current_time)";

    public static final String CREATE_USER = "create table User("
            + "id integer primary key autoincrement,"
            + "name text not null,"
            + "password text not null)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY);
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
