package com.example.ine5424.smartufsc;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;

/**
 * Created by anna on 6/16/17.
 */

public class ApiInterfaceCall extends AsyncTask<Object, Object, Object> {

    private final int id;
    private final Call callable;

    public ApiInterfaceCall(int id, Call call) {
        this.id = id;
        this.callable = call;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Object obj = null;
        try {
            obj = callable.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
    }
}
