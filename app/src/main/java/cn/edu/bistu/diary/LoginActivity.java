package cn.edu.bistu.diary;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.bistu.diary.Data.UserRepository;
import cn.edu.bistu.diary.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserRepository userRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = UserRepository.getInstance(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.login.setOnClickListener(view -> loginOrRegister());
    }

    private void loginOrRegister() {
        String name = binding.username.getText().toString();
        String password = binding.password.getText().toString();
        if (name.length() == 0 || password.length() == 0) {
            Toast.makeText(getApplicationContext(), "用户名和密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = userRepository.login(name, password);
        if (result) {
            Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "登录失败!", Toast.LENGTH_SHORT).show();
        }
    }
}