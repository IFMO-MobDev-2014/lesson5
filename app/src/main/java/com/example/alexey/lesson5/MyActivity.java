package com.example.alexey.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.fadk);
        a=new Vector<String>();
        b=new Vector<String>();
        c=new Vector<String>();


        task = new MyAsyncTask();
        task.execute();
        while (h == 0) {
        }




        Intent intent=new Intent(MyActivity.this,MyActivity15.class);
        startActivity(intent);
        this.onDestroy();

    }




    public static TextView imageView;

}
