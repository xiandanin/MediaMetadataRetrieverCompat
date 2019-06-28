package com.dyhdyh.compat.mmrc.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dyhdyh.manager.assets.AssetFile;
import com.dyhdyh.manager.assets.AssetsManager;

import java.io.File;
import java.util.List;

/**
 * @author dengyuhan
 * created 2019/3/15 11:34
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleHolder> {
    private List<AssetFile> mData;
    private OnItemClickListener mOnItemClickListener;

    public ExampleAdapter(Context context,String dir) {
        this.mData = AssetFile.listFiles(context.getAssets(), dir);
    }

    @Override
    public ExampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button button = new Button(parent.getContext());
        button.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ExampleHolder(button);
    }

    @Override
    public void onBindViewHolder(ExampleHolder holder, int position) {
        final AssetFile assetFile = mData.get(position);
        final String name = assetFile.getName();
        ((Button) holder.itemView).setText(name.substring(0, name.lastIndexOf(".")));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File cacheFile = new File(v.getContext().getExternalCacheDir(), assetFile.getName());
                if (!cacheFile.exists()) {
                    AssetsManager.copyAssetFile(v.getContext(), assetFile.getAssetPath(), cacheFile);
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.oOnItemClick(cacheFile);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void oOnItemClick(File exampleFile);
    }

    static class ExampleHolder extends RecyclerView.ViewHolder {

        public ExampleHolder(View itemView) {
            super(itemView);
        }
    }
}
