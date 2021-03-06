package com.example.just.businesinfo.ImageLoader;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by just on 13.12.2016.
 */

public class ThreadManager {

    public static final int COUNT_CORE = 3;
    private final ExecutorService executorService;

    public ThreadManager() {
        this(Executors.newFixedThreadPool(3));
    }

    public ThreadManager(final ExecutorService executorService) {
        this.executorService = executorService;
    }

    public<Params, Progress, Result> void execute(final Operation<Params, Progress, Result> operation, final Params param, final OnResultCallback<Result, Progress> onResultCallback) {
        final Handler handler = new Handler();
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    final Result result = operation.doing(param, new ProgressCallback<Progress>() {

                        @Override
                        public void onProgressChanged(final Progress progress) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    onResultCallback.onProgressChanged(progress);
                                }
                            });
                        }
                    });
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            onResultCallback.onSuccess(result);
                        }
                    });
                } catch (Exception e) {
                    onResultCallback.onError(e);
                }
            }
        });
    }

}
