package cn.edu.bistu.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cn.edu.bistu.diary.Data.Dao.DiaryInfoDao;
import cn.edu.bistu.diary.Data.DatabaseHelper;
import cn.edu.bistu.diary.Data.UserRepository;
import cn.edu.bistu.diary.Data.Model.Diary;
import cn.edu.bistu.diary.databinding.ActivityDiaryBinding;

public class DiaryActivity extends AppCompatActivity {
    private final static int IMAGE_REQUEST_CODE = 0;
    private ActivityDiaryBinding binding;
    private UserRepository userRepository;
    private final DiaryInfoDao dao = new DiaryInfoDao(new DatabaseHelper(this, "Diary.db", null, 1));
    private int id;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository.getInstance(this);
        binding = ActivityDiaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAuthor();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        initDiary();

        binding.fab.setOnClickListener(view -> {
            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
            update();
        });

        binding.base.cover.setOnClickListener(view -> {
                    Intent intent1 = new Intent(this, PictureActivity.class);
                    startActivityForResult(intent1, IMAGE_REQUEST_CODE);
                }
        );
    }

    @SuppressLint("SetTextI18n")
    private void initAuthor() {
        binding.base.author.setText(userRepository.getUser().getName());
        binding.base.time.setText("");
    }

    private void initDiary() {
        if (id < 0) {
            Toast.makeText(this, "错误的ID", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        Diary diary = dao.getById(id);
        if (diary != null) {
            if (diary.getImagePath() != null && !"".equals(diary.getImagePath())) {
                File file = new File(diary.getImagePath());
                if (file.exists()) {
                    imageUri = Uri.fromFile(file);
                    binding.base.cover.setImageURI(imageUri);
                }
            }
            binding.base.author.setText(diary.getAuthor());
            binding.base.content.setText(diary.getContent());
            binding.base.title.setText(diary.getTitle());
            binding.base.time.setText(diary.getTime());
        }

    }

    private void update() {
        String title = String.valueOf(binding.base.title.getText()); //获取需要储存的值
        String content = String.valueOf(binding.base.content.getText());

        if (title.length() == 0) {   //标题为空给出提示
            Toast.makeText(this, "请输入一个标题", Toast.LENGTH_LONG).show();
        } else {
            Diary diary = new Diary(id, title, null, null, content, null);
            if (imageUri != null) {
                diary.setImagePath(imageUri.getPath());
            }
            dao.update(diary);
        }
//        Log.d("UPDATE", "update: " + values);
    }

    @Override
    protected void onPause() {
        super.onPause();
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_delete) {
            try {
                if (dao.deleteById(id) != 0)
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                this.finish();
            } catch (Exception e) {
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String filename = data.getStringExtra("imagePath");
                imageUri = Uri.fromFile(new File(getFilesDir(), filename));
                Log.d("DIARY", "onActivityResult: " + imageUri.getPath());
                binding.base.cover.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }
}