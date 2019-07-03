package in.xiandan.mmrc.example;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.media.ExifInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import in.xiandan.mmrc.MediaMetadataConfig;
import in.xiandan.mmrc.MediaMetadataKey;
import in.xiandan.mmrc.MediaMetadataResource;
import in.xiandan.mmrc.MediaMetadataRetrieverCompat;
import in.xiandan.mmrc.MediaMetadataRetrieverFactory;
import in.xiandan.mmrc.example.custom.CustomFormatChecker;
import in.xiandan.mmrc.example.custom.CustomKey;
import in.xiandan.mmrc.example.custom.SVGMediaMetadataRetrieverFactory;
import in.xiandan.mmrc.fileformat.UnknownFileFormatException;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText ed;
    RecyclerView rv;
    Spinner spinner;
    Spinner spinner_frame;
    Spinner spinner_size;

    private ThumbnailAdapter mThumbnailAdapter;

    private Disposable mThumbnailDisposable;

    private List<String> mKeys;

    private int mSizeType = CENTER_CROP;
    private int mFetchFrameOption = MediaMetadataKey.OPTION_CLOSEST_SYNC;
    private Class<MediaMetadataRetrieverFactory> mRetrieverFactoryCls;

    private static final int CENTER_CROP = 0;
    private static final int ORIGINAL_SIZE = 1;
    private static final int FLOOR_SCALE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        ed = (EditText) findViewById(R.id.ed);
        rv = (RecyclerView) findViewById(R.id.rv);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner_frame = (Spinner) findViewById(R.id.spinner_frame);
        spinner_size = (Spinner) findViewById(R.id.spinner_size);

        //ed.setText("http://p19.qhimg.com/bdr/__85/t01a05462d1b63252e4.jpg");
        //ed.setText("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");

        //应用全局配置
        MediaMetadataConfig.newBuilder()
                .addFileFormatChecker(new CustomFormatChecker())
                .addCustomRetrieverFactory(new SVGMediaMetadataRetrieverFactory())
                .build()
                .apply();

        mKeys = getAllKeys();

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        final List<MediaMetadataRetrieverFactory> factories = MediaMetadataResource.globalConfig().getFactories();
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, new String[factories.size() + 1]) {
            @Override
            public String getItem(int position) {
                return position == 0 ? "自动" : factories.get(position - 1).getClass().getSimpleName().replace("Factory", "");
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mRetrieverFactoryCls = null;
                } else {
                    try {
                        mRetrieverFactoryCls = (Class<MediaMetadataRetrieverFactory>) factories.get(position - 1).getClass();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final String[] fetchFrame = new String[]{"OPTION_CLOSEST_SYNC", "OPTION_CLOSEST", "OPTION_PREVIOUS_SYNC", "OPTION_NEXT_SYNC"};
        spinner_frame.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, fetchFrame));
        spinner_frame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    final Field field = MediaMetadataKey.class.getField(fetchFrame[position]);
                    mFetchFrameOption = field.getInt(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] sizes = new String[]{"CenterCrop", "原始尺寸", "向下缩放"};
        spinner_size.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, sizes));
        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSizeType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkPermission();

        bindExampleRecyclerView((RecyclerView) findViewById(R.id.rv_image), "sample-image", 5);
        bindExampleRecyclerView((RecyclerView) findViewById(R.id.rv_audio), "sample-audio", 5);
        bindExampleRecyclerView((RecyclerView) findViewById(R.id.rv_video), "sample-video", 4);
        bindExampleRecyclerView((RecyclerView) findViewById(R.id.rv_custom), "sample-custom", 4);

    }

    private List<String> getAllKeys() {
        List<String> keys = new ArrayList<>();
        final Field[] fields = MediaMetadataKey.class.getFields();
        for (Field field : fields) {
            if (!field.getName().startsWith("OPTION")) {
                try {
                    keys.add((String) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        final Field[] exifFields = ExifInterface.class.getFields();
        for (Field field : exifFields) {
            if (field.getName().startsWith("TAG")) {
                try {
                    keys.add((String) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        //自定义
        final Field[] customFields = CustomKey.class.getFields();
        for (Field field : customFields) {
            if (field.getName().startsWith("METADATA_KEY")) {
                try {
                    keys.add((String) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return keys;
    }

    private void bindExampleRecyclerView(RecyclerView recyclerView, String dir, int spanCount) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        final ExampleAdapter adapter = new ExampleAdapter(this, dir);
        adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void oOnItemClick(File exampleFile) {
                onItemClickMediaMetadata(exampleFile.getAbsolutePath());
            }
        });
        recyclerView.setAdapter(adapter);

    }


    public void clickMediaMetadata(View v) {
        final String input = ed.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            onItemClickMediaMetadata(input);
        }
    }

    private void onItemClickMediaMetadata(String input) {
        if (mThumbnailDisposable != null && !mThumbnailDisposable.isDisposed()) {
            mThumbnailDisposable.dispose();
        }

        final MediaMetadataRetrieverCompat mmrc = new MediaMetadataRetrieverCompat();

        Observable.just(input).flatMap((Function<String, ObservableSource<String>>) url -> {
            //设置数据源
            mmrc.setDataSourceOrThrow(URLSource.create(MainActivity.this, url), mRetrieverFactoryCls);
            //异步获取所有能拿到的值
            return obtainMetadataInfo(mmrc);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(info -> {
                    tv.setText(info);
                    return info;
                })
                .map(s -> {
                    final long duration = mmrc.extractMetadataLong(MediaMetadataKey.DURATION, 0);
                    //每1秒取一帧
                    final long interval = 1000;
                    //最少取一帧 最多取10帧
                    final int itemCount = (int) Math.min(Math.max(1, duration / interval), 10);
                    mThumbnailAdapter = new ThumbnailAdapter(itemCount);
                    rv.setAdapter(mThumbnailAdapter);
                    return itemCount;
                })
                .observeOn(Schedulers.io())
                .flatMap((Function<Integer, ObservableSource<ThumbnailBitmap>>) itemCount -> {
                    //异步获取帧缩略图
                    return obtainThumbnail(mmrc, itemCount);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ThumbnailBitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mThumbnailDisposable = d;
                        tv.setText(null);
                    }

                    @Override
                    public void onNext(ThumbnailBitmap thumbnail) {
                        //缩略图更新到RecyclerView
                        mThumbnailAdapter.setThumbnail(thumbnail);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e instanceof UnknownFileFormatException) {
                            Toast.makeText(MainActivity.this, "不支持此格式", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FileNotFoundException) {
                            Toast.makeText(MainActivity.this, "此文件不存在", Toast.LENGTH_SHORT).show();
                        } else if (e instanceof IOException) {
                            Toast.makeText(MainActivity.this, "检索器创建失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "获取失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onComplete() {
                        mmrc.release();
                    }
                });
    }

    private Observable<ThumbnailBitmap> obtainThumbnail(MediaMetadataRetrieverCompat mmrc, int itemCount) {
        //每500毫秒取一帧
        final long interval = 500;
        final int size = getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
        //取帧是耗时的操作,需要放在子线程
        return Observable.create(new ObservableOnSubscribe<ThumbnailBitmap>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ThumbnailBitmap> s) throws Exception {
                //异步获取缩略图
                for (int i = 0; i < itemCount; i++) {
                    long millis = i * interval;
                    Bitmap atTime;
                    if (ORIGINAL_SIZE == mSizeType) {
                        atTime = mmrc.getFrameAtTime(millis, mFetchFrameOption);
                    } else if (FLOOR_SCALE == mSizeType) {
                        atTime = mmrc.getScaledFrameAtTime(millis, mFetchFrameOption, size, size);
                    } else {
                        atTime = mmrc.getCenterCropFrameAtTime(millis, mFetchFrameOption, size, size);
                    }
                    s.onNext(new ThumbnailBitmap(i, atTime));
                }
                s.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取所有能拿到的值
     */
    private Observable<String> obtainMetadataInfo(final MediaMetadataRetrieverCompat mmrc) {
        return Observable.just("").map(new Function<String, String>() {
            @Override
            public String apply(String empty) throws Exception {
                StringBuilder sb = new StringBuilder();
                sb.append("Retriever：");
                sb.append(mmrc.getRetriever().getClass().getSimpleName());
                sb.append("\n");
                for (String key : mKeys) {
                    String value = mmrc.extractMetadata(key);
                    if (value != null) {
                        sb.append("\n");
                        sb.append(key);
                        sb.append("：");
                        sb.append(value);
                    }
                }
                return sb.toString();
            }
        });

    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

}
