package in.xiandan.mmrc.example;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * author  dengyuhan
 * created 2017/5/27 14:22
 */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.Holder> {
    private SimpleDateFormat mFormat;
    private ThumbnailBitmap[] mThumbnails;

    public ThumbnailAdapter(int thumbnailCount) {
        this.mThumbnails = new ThumbnailBitmap[thumbnailCount];
        this.mFormat = new SimpleDateFormat("mm:ss.SSS");
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ThumbnailBitmap item = mThumbnails[position];
        if (item != null) {
            final Bitmap bitmap = item.getBitmap();
            if (bitmap == null) {
                holder.iv_thumbnail.setImageResource(R.color.colorPlaceholder);
                holder.tv_time.setText(String.valueOf(position));
            } else {
                holder.iv_thumbnail.setImageBitmap(bitmap);
                holder.tv_time.setText(String.format("%d - %dx%d", position, bitmap.getWidth(), bitmap.getHeight()));
            }
        } else {
            holder.iv_thumbnail.setImageBitmap(null);
            holder.tv_time.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mThumbnails.length;
    }

    public void setThumbnail(ThumbnailBitmap item) {
        mThumbnails[item.getIndex()] = item;
        notifyItemChanged(item.getIndex());
    }

    static class Holder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_time;

        public Holder(View itemView) {
            super(itemView);
            iv_thumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
