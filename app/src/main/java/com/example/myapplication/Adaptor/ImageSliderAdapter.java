package com.example.myapplication.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.example.myapplication.R;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private Context context;
    private List<byte[]> imageBytesList;

    public ImageSliderAdapter(Context context, List<byte[]> imageBytesList) {
        this.context = context;
        this.imageBytesList = imageBytesList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_slider, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);

        // Convert byte array to Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytesList.get(position), 0, imageBytesList.get(position).length);
        imageView.setImageBitmap(bitmap);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return imageBytesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
