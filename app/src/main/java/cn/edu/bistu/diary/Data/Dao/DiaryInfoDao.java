package cn.edu.bistu.diary.Data.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.edu.bistu.diary.Data.DatabaseHelper;
import cn.edu.bistu.diary.Data.Model.Diary;

@SuppressLint("Range")
public class DiaryInfoDao {
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//配置时间格式;
    private final DatabaseHelper dbHelper;

    public DiaryInfoDao(DatabaseHelper helper) {
        dbHelper = helper;
    }

    public Diary getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();     //通过dbHelper获得可读文件
        Cursor cursor = db.query("Diary", new String[]{"title", "author", "content", "imagePath", "date"},
                "id=?", new String[]{id + ""}, null, null, null);
        Diary info = null;
        if (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String author = cursor.getString(cursor.getColumnIndex("author"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String imagePath = cursor.getString(cursor.getColumnIndex("imagePath"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            info = new Diary(id, title, author, date, content, imagePath);
        }
        cursor.close();
        return info;
    }

    public List<Diary> getDiaryInfoByAuthor(String author) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Diary where author=?", new String[]{author});
        LinkedList<Diary> info = new LinkedList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String name = cursor.getString(cursor.getColumnIndex("author"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            info.add(new Diary(id, title, name, date));
        }
        cursor.close();
        return info;
    }

    private ContentValues getValues(Diary diary) {
        String simpleDate = formatter.format(new Date());
        ContentValues values = new ContentValues();
        values.put("title", diary.getTitle());
        values.put("content", diary.getContent());
        values.put("date", simpleDate);
        values.put("imagePath", diary.getImagePath());
        return values;
    }

    public int update(Diary diary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getValues(diary);
        return db.update("Diary", values, "id=?", new String[]{diary.getId() + ""});  //将值传入数据库中
    }

    public void insert(Diary diary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getValues(diary);
        values.put("author", diary.getAuthor());
        db.insert("Diary", null, values);
    }

    public int deleteById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("Diary", "id=?", new String[]{id + ""});
    }
}
