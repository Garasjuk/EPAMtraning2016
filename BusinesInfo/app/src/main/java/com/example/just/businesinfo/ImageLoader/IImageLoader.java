package com.example.just.businesinfo.ImageLoader;

import android.widget.ImageView;

public interface IImageLoader {
    void drawBitmap(final ImageView imageView, final String imageUrl);

    class Impl {
        public static IImageLoader newInstance() {
            return new ImageLoaderImpl();
        }
    }
}
