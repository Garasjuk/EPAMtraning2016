package com.example.just.businesinfo;

import android.app.Application;
import android.os.AsyncTask;

import com.example.just.businesinfo.ImageLoader.IImageLoader;
import com.example.just.businesinfo.utils.ContextGodObject;
import com.example.just.businesinfo.utils.ContextHolder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {

    private IImageLoader iImageLoader;

    public IImageLoader getiImageLoader() {
        if (iImageLoader == null) {
            iImageLoader = IImageLoader.Impl.newInstance();
        }
        return iImageLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {

            @Override
            public void run() {
            }
        });

        ContextHolder.set(this);
        ContextGodObject.getInstance().setContext(this);

        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(this);

        ImageLoader.getInstance().init(config);
    }
}
