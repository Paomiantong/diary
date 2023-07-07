package cn.edu.bistu.diary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

import cn.edu.bistu.diary.Data.Dao.DiaryInfoDao;
import cn.edu.bistu.diary.Data.DatabaseHelper;
import cn.edu.bistu.diary.Data.UserRepository;
import cn.edu.bistu.diary.Data.Model.Diary;
import cn.edu.bistu.diary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final List<Diary> diaryList = new LinkedList<>();
    private UserRepository userRepository;
    private String author;
    private final DiaryInfoDao dao = new DiaryInfoDao(new DatabaseHelper(this, "Diary.db", null, 1));
    private DiaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        userRepository = UserRepository.getInstance(this);
        setContentView(binding.getRoot());

        onAuthentication();
        loadDiaryInfo();
        adapter = new DiaryListAdapter(this, android.R.layout.simple_list_item_1, R.layout.diary_list_item, diaryList);

        binding.list.setAdapter(adapter);
        binding.list.setOnItemClickListener((adapterView, view, i, l) -> {
            Diary item = diaryList.get(i);
            Intent intent = new Intent(this, DiaryActivity.class);
            intent.putExtra("id", item.getId());
            startActivity(intent);
        });

        binding.list.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Diary item = diaryList.get(i);
            alertClick(item.getId());
            return true;
        });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        });

        binding.login.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    public void alertClick(int id) {
        //创建 一个提示对话框的构造者对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否删除该日记");//设置弹出对话框的内容
        builder.setCancelable(true);//能否被取消
        //正面的按钮（肯定）
        builder.setPositiveButton("确认", (dialog, which) -> {
            try {
                dao.deleteById(id);
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                loadDiaryInfo();// 刷新显示
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        //反面的按钮（否定）
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void onAuthentication() {
        userRepository.authFromPref();
        author = userRepository.getUser().getName();
        binding.helloName.setText("你好:" + author);
        if (userRepository.isLoggedIn()) { // 如果登录了就隐藏登录按钮
            binding.login.setVisibility(View.INVISIBLE);
        } else {
            binding.login.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("Range")
    private void loadDiaryInfo() {
        diaryList.clear();
        diaryList.addAll(dao.getDiaryInfoByAuthor(author));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onAuthentication();
        loadDiaryInfo();
        adapter.notifyDataSetChanged();
    }
}