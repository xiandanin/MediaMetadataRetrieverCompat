package com.dyhdyh.compat.mmrc.example;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyhdyh.compat.mmrc.MediaMetadataRetrieverCompat;
import com.dyhdyh.compat.mmrc.impl.FFmpegMediaMetadataRetrieverImpl;

import java.io.File;
import java.io.FileNotFoundException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    TextView tv;
    RadioGroup rg;
    EditText ed;
    RecyclerView rv;

    private File testFile;

    private ThumbnailAdapter mThumbnailAdapter;
    private MediaMetadataRetrieverCompat mmrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        rg = (RadioGroup) findViewById(R.id.rg);
        ed = (EditText) findViewById(R.id.ed);
        rv = (RecyclerView) findViewById(R.id.rv);
        rg.setOnCheckedChangeListener(this);

        checkPermission();

        testFile = new File(getCacheDir(), "test.mp4");
        FileUtils.copyAssetFile(this, "test.mp4", testFile);

        mmrc = MediaMetadataRetrieverCompat.create();
        //mmrc = new MediaMetadataRetrieverCompat(MediaMetadataRetrieverCompat.RETRIEVER_ANDROID);
        //mmrc = new MediaMetadataRetrieverCompat(MediaMetadataRetrieverCompat.RETRIEVER_FFMPEG);
    }

    public void clickMediaMetadata(View v) {
        String toastText = String.format("当前使用%s解析器", mmrc.getMediaMetadataRetriever() instanceof FFmpegMediaMetadataRetrieverImpl ? "FFmpeg" : "原生");
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

        String path = TextUtils.isEmpty(ed.getText()) ? testFile.getAbsolutePath() : ed.getText().toString();

        //设置路径
        try {
            mmrc.setMediaDataSource(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        //获取元数据
        String width = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_WIDTH);
        String height = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_HEIGHT);
        String rotation = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_ROTATION);
        String numTracks = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_NUM_TRACKS);
        String title = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_TITLE);
        String album = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUM);
        String albumArtist = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUMARTIST);
        String author = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_AUTHOR);
        String duration = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_DURATION);
        String framerate = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_CAPTURE_FRAMERATE);

        bindMetadata(width, height, rotation, numTracks, title, album, albumArtist, author, duration, framerate);

        try {
            //获取缩略图
            final int thumbnailCount = (int) (Long.parseLong(duration) / 1000);
            final int thumbnailWidth = getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
            final int thumbnailHeight = (int) ((float) thumbnailWidth / Integer.parseInt(width) * Integer.parseInt(height));
            bindThumbnail(thumbnailCount, thumbnailWidth, thumbnailHeight);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "获取缩略图失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindMetadata(String width, String height, String rotation, String numTracks, String title, String album, String albumArtist, String author, String duration, String frameRate) {
        StringBuilder sb = new StringBuilder();
        sb.append("宽：");
        sb.append(width);
        sb.append("\n高：");
        sb.append(height);
        sb.append("\n时长：");
        sb.append(duration);
        sb.append("\n轨道数量：");
        sb.append(numTracks);
        sb.append("\n帧率：");
        sb.append(frameRate);
        sb.append("\n旋转角度：");
        sb.append(rotation);
        sb.append("\n标题：");
        sb.append(title);
        sb.append("\n专辑：");
        sb.append(album);
        sb.append("\n艺术家：");
        sb.append(albumArtist);
        sb.append("\n作者：");
        sb.append(author);
        tv.setText(sb.toString());
    }

    private void bindThumbnail(final int count, final int width, final int height) {
        mThumbnailAdapter = new ThumbnailAdapter(count);//每秒取1帧
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(mThumbnailAdapter);
        //取帧是耗时的操作,需要放在子线程
        Observable.create(new ObservableOnSubscribe<ThumbnailBitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ThumbnailBitmap> s) throws Exception {
                try {
                    for (int i = 0; i < count; i++) {
                        //Bitmap atTime = mmrc.getFrameAtTime();
                        //注意这里传的是微秒
                        Bitmap atTime = mmrc.getScaledFrameAtTime(i * 1000 * 1000, MediaMetadataRetrieverCompat.OPTION_CLOSEST,
                                width, height);
                        s.onNext(new ThumbnailBitmap(i, atTime));
                    }
                } catch (Exception e) {
                    s.onError(e);
                    e.printStackTrace();
                }
                s.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ThumbnailBitmap>() {
                    @Override
                    public void accept(@NonNull ThumbnailBitmap bitmap) throws Exception {
                        //刷新adapter
                        mThumbnailAdapter.setThumbnail(bitmap.getIndex(), bitmap.getBitmap());
                    }
                }).subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mmrc.release();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_ffmpeg:
                mmrc = new MediaMetadataRetrieverCompat(MediaMetadataRetrieverCompat.RETRIEVER_FFMPEG);
                break;
            case R.id.rb_android:
                mmrc = new MediaMetadataRetrieverCompat(MediaMetadataRetrieverCompat.RETRIEVER_ANDROID);
                break;
            default:
                mmrc = new MediaMetadataRetrieverCompat();
                break;
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }
}
