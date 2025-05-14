package com.wowcodes.supreme.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wowcodes.supreme.R;
import java.util.List;
import java.util.Objects;

public class ImagesAdapter extends PagerAdapter {
    Context context;
    List<String> images;
    LayoutInflater mLayoutInflater;

    public ImagesAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.images_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        try {
            Glide.with(context)
                    .load(images.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(imageView);
        } catch (Exception ignore) {}

        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}