package com.example.just.businesinfo.ImageLoader;

public interface Operation <Params, Progress, Result> {

    Result doing(Params params, ProgressCallback<Progress> progressCallback) throws Exception;
}
