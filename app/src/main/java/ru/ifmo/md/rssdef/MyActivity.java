package ru.ifmo.md.rssdef;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class MyActivity extends Activity {

    static DataStorage ds;
    static ListAdapter listAdapter;
    static ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        lv = (ListView) findViewById(R.id.list_view);
        ds = new DataStorage(this);
        listAdapter = new ListAdapter(this, ds);
        lv.setAdapter(listAdapter);
    }

    public void addUrlAction(View v) {
        MyDialog md = new MyDialog(this);
        md.show();
    }


}
