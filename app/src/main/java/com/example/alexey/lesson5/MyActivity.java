package com.example.alexey.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Vector;


public class MyActivity extends Activity {
    MyAsyncTask task;
    static String getd = "нахера";
    public static int h = 0;
    public static Vector<String> a;
    public static Vector<String> b;
    public static Vector<String> c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a = new Vector<String>();
        b = new Vector<String>();
        c = new Vector<String>();


        task = new MyAsyncTask();
        task.execute();

        setContentView(R.layout.fadk);
        while (h == 0) {
        }

        Intent intent = new Intent(MyActivity.this, MyActivity15.class);
        startActivity(intent);
    }
}
