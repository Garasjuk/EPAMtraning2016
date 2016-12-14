package com.example.just.businesinfo.ImageLoader;

/**
 * Created by just on 13.12.2016.
 */

public interface OnResultCallback <Result, Progress> extends ProgressCallback<Progress> {
    void onSuccess(Result result);

    void onError(Exception e);
}
