package com.example.just.asynctaskapplication.ui;

public interface IResultCallback<Result> {

    void onSuccess(Result result);

    void onError(Exception exception);
}
