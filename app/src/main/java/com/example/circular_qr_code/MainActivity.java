package com.example.circular_qr_code;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ivCode=findViewById(R.id.iv_code);
        try {
            Bitmap bitmap = CircularQRCodeUtil.generateQRCodeImage("http://www.baidu.com", 300, 300, Color.parseColor("#01499E"), 0);
            ivCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}