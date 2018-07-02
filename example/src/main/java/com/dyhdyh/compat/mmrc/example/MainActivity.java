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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dyhdyh.compat.mmrc.MediaMetadataRetrieverCompat;
import com.dyhdyh.manager.assets.AssetFile;
import com.dyhdyh.manager.assets.AssetsManager;
import com.dyhdyh.widget.loading.bar.LoadingBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    TextView tv;
    RadioGroup rg;
    EditText ed;
    RecyclerView rv;
    View layout_info;

    private File testFile;

    private ThumbnailAdapter mThumbnailAdapter;
    private MediaMetadataRetrieverCompat mmrc;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        rg = (RadioGroup) findViewById(R.id.rg);
        ed = (EditText) findViewById(R.id.ed);
        rv = (RecyclerView) findViewById(R.id.rv);
        layout_info = findViewById(R.id.layout_info);
        rg.setOnCheckedChangeListener(this);

        mDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        checkPermission();

        AssetsManager.copyAsset(this, new AssetFile(), getExternalCacheDir());

        testFile = new File(getExternalCacheDir(), "test.mp4");

        mmrc = MediaMetadataRetrieverCompat.create();
        //mmrc = MediaMetadataRetrieverCompat.create(MediaMetadataRetrieverCompat.RETRIEVER_FFMPEG);
        //mmrc = MediaMetadataRetrieverCompat.create(MediaMetadataRetrieverCompat.RETRIEVER_ANDROID);
    }

    public void clickMediaMetadata(View v) {
        final String path = TextUtils.isEmpty(ed.getText()) ? testFile.getAbsolutePath() : ed.getText().toString();

        //这里示例用子线程 实际开发中根据需求
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> s) throws Exception {
                try {
                    boolean isOnlineVideo = path.startsWith("http");
                    if (isOnlineVideo) {
                        mmrc.setDataSource(path, null);
                    } else {
                        mmrc.setDataSource(new File(path));
                    }
                    //获取缩略图
                    long duration = mmrc.extractMetadataLong(MediaMetadataRetrieverCompat.METADATA_KEY_DURATION);
                    int width = mmrc.extractMetadataInt(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_WIDTH);
                    int height = mmrc.extractMetadataInt(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_HEIGHT);
                    int thumbnailCount = (int) Math.max(1, Math.min(10, duration / 1000));
                    int thumbnailWidth = getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
                    final int thumbnailHeight = (int) ((float) thumbnailWidth / width * height);
                    buildThumbnail(thumbnailCount, thumbnailWidth, thumbnailHeight);
                    s.onNext(buildMetadataInfo());
                } catch (Exception e) {
                    e.printStackTrace();
                    s.onError(e);
                }
                s.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LoadingBar.make(layout_info).show();
                    }

                    @Override
                    public void onNext(String s) {
                        LoadingBar.cancel(layout_info);
                        tv.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingBar.cancel(layout_info);
                        Toast.makeText(MainActivity.this, "解析失败 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private String buildMetadataInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("API类型：");
        sb.append(mmrc.getMediaMetadataRetriever().getClass().getSimpleName());
        sb.append("\n");
        String width = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_WIDTH);
        if (width != null) {
            sb.append("\n宽：");
            sb.append(width);
        }
        String height = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_HEIGHT);
        if (height != null) {
            sb.append("\n高：");
            sb.append(height);
        }
        long duration = mmrc.extractMetadataLong(MediaMetadataRetrieverCompat.METADATA_KEY_DURATION);
        if (duration >= 0) {
            sb.append("\n时长：");
            sb.append(mDateFormat.format(duration));
            sb.append(" （");
            sb.append(duration);
            sb.append("毫秒）");
        }
        String rotation = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_VIDEO_ROTATION);
        if (rotation != null) {
            sb.append("\n旋转角度：");
            sb.append(rotation);
        }
        String numTracks = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_NUM_TRACKS);
        if (numTracks != null) {
            sb.append("\n轨道数：");
            sb.append(numTracks);
        }
        String frameRate = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_CAPTURE_FRAMERATE);
        if (frameRate != null) {
            sb.append("\n帧率：");
            sb.append(frameRate);
        }
        String title = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_TITLE);
        if (title != null) {
            sb.append("\n标题：");
            sb.append(title);
        }
        String album = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUM);
        if (album != null) {
            sb.append("\n专辑：");
            sb.append(album);
        }
        String albumArtist = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_ALBUMARTIST);
        if (albumArtist != null) {
            sb.append("\n艺术家：");
            sb.append(albumArtist);
        }
        String author = mmrc.extractMetadata(MediaMetadataRetrieverCompat.METADATA_KEY_AUTHOR);
        if (albumArtist != null) {
            sb.append("\n作者：");
            sb.append(author);
        }
        return sb.toString();
    }

    private void buildThumbnail(final int count, final int width, final int height) {
        mThumbnailAdapter = null;
        //取帧是耗时的操作,需要放在子线程
        Observable.create(new ObservableOnSubscribe<ThumbnailBitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ThumbnailBitmap> s) throws Exception {
                try {
                    for (int i = 0; i < count; i++) {
                        //Bitmap atTime = mmrc.getFrameAtTime();
                        //注意这里传的是微秒
                        Bitmap atTime = mmrc.getScaledFrameAtTime(i * 1000 * 1000,
                                MediaMetadataRetrieverCompat.OPTION_CLOSEST, width, height);
                        s.onNext(new ThumbnailBitmap(i, atTime));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    s.onError(e);
                }
                s.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<ThumbnailBitmap>() {

                    @Override
                    public void onNext(ThumbnailBitmap bitmap) {
                        if (mThumbnailAdapter == null) {
                            mThumbnailAdapter = new ThumbnailAdapter(count);//每秒取1帧
                            rv.setAdapter(mThumbnailAdapter);
                        }
                        //刷新adapter
                        mThumbnailAdapter.setThumbnail(bitmap.getIndex(), bitmap.getBitmap());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "获取缩略图失败 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
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
                mmrc = MediaMetadataRetrieverCompat.create(MediaMetadataRetrieverCompat.RETRIEVER_FFMPEG);
                break;
            case R.id.rb_android:
                mmrc = MediaMetadataRetrieverCompat.create(MediaMetadataRetrieverCompat.RETRIEVER_ANDROID);
                break;
            default:
                mmrc = MediaMetadataRetrieverCompat.create();
                break;
        }

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void clickVideo(MenuItem item) {
        ed.setText(new File(getExternalCacheDir(), "test.mp4").getAbsolutePath());
        clickMediaMetadata(null);
    }

    public void clickOnlineVideo(MenuItem item) {
        //ed.setText("http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4");
        ed.setText("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        clickMediaMetadata(null);
    }

    public void clickAudio(MenuItem item) {
        ed.setText(new File(getExternalCacheDir(), "test_audio.mp3").getAbsolutePath());
        clickMediaMetadata(null);
    }
}
