package cn.edu.bistu.diary;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.bistu.diary.Data.UserRepository;
import cn.edu.bistu.diary.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository.getInstance(this);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onAuthentication();

        binding.logout.setOnClickListener(view -> {
            userRepository.logout();
            finish();
        });

        binding.changePassword.setOnClickListener(view -> changePassword());
    }

    private void onAuthentication() {
        if (!userRepository.isLoggedIn()) { // 如果没有登录就不能修改用户设置
            binding.userSettings.setVisibility(View.INVISIBLE);
        } else {
            binding.userSettings.setVisibility(View.VISIBLE);
        }
        String text = "当前登录用户:" + userRepository.getUser().getName();
        binding.username.setText(text);
    }

    private void changePassword() {
        String password = String.valueOf(binding.password.getText());
        if (password.length() == 0) {
            Toast.makeText(this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        userRepository.changePassword(password);
        Toast.makeText(this, "密码修改成功!", Toast.LENGTH_SHORT).show();
    }
}