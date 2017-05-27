package com.dyhdyh.compat.mmrc.example;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * author  dengyuhan
 * created 2017/5/27 14:22
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.Holder> {
    private Bitmap[] bitmaps;

    public ThumbnailAdapter(int thumbnailCount) {
        this.bitmaps = new Bitmap[thumbnailCount];
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (bitmaps[position] == null) {
            holder.iv_thumbnail.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.iv_thumbnail.setImageBitmap(bitmaps[position]);
        }
    }

    @Override
    public int getItemCount() {
        return bitmaps.length;
    }

    public void setThumbnail(int index, Bitmap bitmap) {
        bitmaps[index] = bitmap;
        notifyItemChanged(index);
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;

        public Holder(View itemView) {
            super(itemView);
            iv_thumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        }
    }
}
