package com.example.just.asynctaskapplication.ui;

/**
 * Created by just on 20.10.2016.
 */

public abstract class ITask<Params, Progress, Result> {

    public abstract Result doInBackground(Params params, IProgressCallback<Progress> progressCallback);
}
