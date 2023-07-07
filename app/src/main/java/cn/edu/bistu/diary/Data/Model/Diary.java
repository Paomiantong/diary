package cn.edu.bistu.diary.Data.Model;

public class Diary {
    private int id;
    private String title;
    private String author;
    private String time;
    private String content;
    private String imagePath;

    public Diary(int id, String title, String author, String time, String content, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.time = time;
        this.content = content;
        this.imagePath = imagePath;
    }

    public Diary(int id, String title, String author, String time) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
