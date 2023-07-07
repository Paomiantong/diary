package cn.edu.bistu.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cn.edu.bistu.diary.Data.Dao.DiaryInfoDao;
import cn.edu.bistu.diary.Data.DatabaseHelper;
import cn.edu.bistu.diary.Data.UserRepository;
import cn.edu.bistu.diary.Data.Model.Diary;
import cn.edu.bistu.diary.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {
    private final static int IMAGE_REQUEST_CODE = 0;
    private ActivityAddBinding binding;
    private UserRepository userRepository;
    private String author;
    private final DiaryInfoDao dao = new DiaryInfoDao(new DatabaseHelper(this, "Diary.db", null, 1));
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository.getInstance(this);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAuthor();

        binding.fab.setOnClickListener(view -> save());
        binding.base.cover.setOnClickListener(view -> {
                    Intent intent1 = new Intent(this, PictureActivity.class);
                    startActivityForResult(intent1, IMAGE_REQUEST_CODE);
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void initAuthor() {
        author = userRepository.getUser().getName();
        binding.base.author.setText(author);
        binding.base.time.setText("");
    }

    private void save() {
        String title = String.valueOf(binding.base.title.getText()); //获取需要储存的值
        String content = String.valueOf(binding.base.content.getText());
        if (title.length() == 0) {   //标题为空给出提示
            Toast.makeText(this, "请输入一个标题", Toast.LENGTH_LONG).show();
        } else {
            Diary diary = new Diary(0, title, author, null, content, null);
            if (imageUri != null)
                diary.setImagePath(imageUri.getPath());
            dao.insert(diary);
        }
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String filename = data.getStringExtra("imagePath");
                imageUri = Uri.fromFile(new File(getFilesDir(), filename));
                binding.base.cover.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }
}