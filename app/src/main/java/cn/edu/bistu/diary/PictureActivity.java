package cn.edu.bistu.diary;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.bistu.diary.databinding.ActivityPictureBinding;

public class PictureActivity extends AppCompatActivity {
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int ALBUM_REQUEST_CODE = 1;
    //用于保存拍照图片的uri
    private Uri mCameraUri = null;
    private final boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;
    private ActivityPictureBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!isAndroidQ) {
            Toast.makeText(this, "暂时不持支Android 10以下的系统进行拍照", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.takePhoto.setOnClickListener(view -> checkPermissionAndCamera());
        binding.chooseFromAlbum.setOnClickListener(view -> openAlbum());
        binding.submit.setOnClickListener(view -> saveResultAndBack());
    }

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            mCameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            if (mCameraUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private void openAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    binding.picture.setImageURI(mCameraUri);
                    break;
                case ALBUM_REQUEST_CODE:
                    assert data != null;
                    mCameraUri = data.getData();
                    binding.picture.setImageURI(mCameraUri);
                    break;
            }
        } else {
            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
        }
    }

    private void saveResultAndBack() {
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()); // 目标文件名
        try {
            // 创建目标文件对象
            File destinationFile = new File(getFilesDir(), filename);
            // 复制文件
            InputStream inputStream = getContentResolver().openInputStream(mCameraUri);
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            // 文件复制成功
            Intent intent = new Intent();
            intent.putExtra("imagePath", filename);
            setResult(RESULT_OK, intent);
            Log.d("CAPTURE", "saveResultAndBack: path=" + filename);
            finish();
        } catch (Exception e) {
            // 文件复制失败
            e.printStackTrace();
            setResult(RESULT_CANCELED);
        }
    }
}