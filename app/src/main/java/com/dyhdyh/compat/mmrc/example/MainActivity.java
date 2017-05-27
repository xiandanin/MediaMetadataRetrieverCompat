package com.dyhdyh.compat.mmrc.example;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.dyhdyh.compat.mmrc.impl.CodecMediaMetadataRetrieverImpl;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    CodecMediaMetadataRetrieverImpl impl;

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            impl = new CodecMediaMetadataRetrieverImpl();
        }
    }

    public void click(View v){
        File file = new File(Environment.getExternalStorageDirectory(), "1.mp4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            impl.setDataSource(file.getAbsolutePath());
            Bitmap atTime = impl.getFrameAtTime();
            iv.setImageBitmap(atTime);
        }
    }
}
