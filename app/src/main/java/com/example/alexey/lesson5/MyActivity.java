package com.example.alexey.lesson5;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Vector;


public class MyActivity extends Activity implements AppReceiver.Receiver {
    MyAsyncTask task;
    public  DataBase sqh;
    public static SQLiteDatabase sqdb;
    public static int h = 0;
    private AppReceiver mReceiver;
    private ProgressBar mProgress;

    private static final String DATABASE_NAME = "qgacat_database.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity15);

        sqh = new DataBase(this);
        sqdb = sqh.getWritableDatabase();

/*
        ContentValues cv=new ContentValues();
        cv.put(DataBase.POSTNAME,"hardware");
        cv.put(DataBase.EMAIL,"http://news.yandex.ru/hardware.rss");
        sqdb.insert(DataBase.TABLE_NAME2,null,cv);
         cv=new ContentValues();
        cv.put(DataBase.POSTNAME,"computers");
        cv.put(DataBase.EMAIL,"http://news.yandex.ru/computers.rss");
        sqdb.insert(DataBase.TABLE_NAME2,null,cv);
         cv=new ContentValues();
        cv.put(DataBase.POSTNAME,"world");
        cv.put(DataBase.EMAIL,"http://news.yandex.ru/world.rss");
        sqdb.insert(DataBase.TABLE_NAME2,null,cv);
         cv=new ContentValues();
        cv.put(DataBase.POSTNAME,"games");
        cv.put(DataBase.EMAIL,"http://news.yandex.ru/games.rss");
        sqdb.insert(DataBase.TABLE_NAME2,null,cv);

*/
        ListView lv=(ListView) findViewById(R.id.listView_start);
        final Cursor cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME2, null, null, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
                new String[]{DataBase.POSTNAME}, new int[]{android.R.id.text1});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
               // Intent intent = new Intent(MyActivity.this, MyActivity15.class);

                TextView tv=(TextView) itemClicked;
                String s=tv.getText().toString();

                //cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME, null, null, null, null, null, null);
                boolean t=true;
                cursor.moveToFirst();
                while(cursor.moveToNext()&&t){
                    String y=cursor.getString(cursor.getColumnIndex(DataBase.POSTNAME));
                    if (y!=null&&y.equals(s)) {
                        s = cursor.getString(cursor.getColumnIndex(DataBase.EMAIL));
                        t=false;
                    }
                }
                //String name = cursor1.getString(cursor1.getColumnIndex(DataBase.EMAIL));



                final Intent intent = new Intent("SOME_COMMAND_ACTION", null, MyActivity.this, IServise.class);
                intent.putExtra(Constants.RECEIVER, mReceiver).putExtra("task", s);
                startService(intent);

                setContentView(R.layout.fadk);
                mProgress = (ProgressBar) findViewById(R.id.progressBar);


      //          startActivity(intent);
                onDestroy();
            }
        });




    }

    @Override
    protected void onStop() {
        super.onStop();
        sqdb.close();
        sqh.close();
    }

    public void go(View view) {
        EditText editText = (EditText) findViewById(R.id.editTextf);
        String name=editText.getText().toString();
        editText = (EditText) findViewById(R.id.editText2f);
        String link=editText.getText().toString();


        ContentValues cv=new ContentValues();
        cv.put(DataBase.POSTNAME,name);
        cv.put(DataBase.EMAIL,link);
        sqdb.insert(DataBase.TABLE_NAME2,null,cv);



        ListView lv=(ListView) findViewById(R.id.listView_start);
        final Cursor cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME2, null, null, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
                new String[]{DataBase.POSTNAME}, new int[]{android.R.id.text1});
        lv.setAdapter(adapter);

    }


    @Override
    public void onReceiveResult(int resultCode, Bundle data) {

        Log.i("Result","got");
        switch (resultCode) {
            case 2:
                mProgress.setVisibility(View.VISIBLE);
                break;
            case 5:
                mProgress.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(this, MyActivity15.class);
                startActivity(intent1);
                setContentView(R.layout.activity_my_activity15);

                ListView lv=(ListView) findViewById(R.id.listView_start);
                final Cursor cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME2, null, null, null, null, null, null);

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor,
                        new String[]{DataBase.POSTNAME}, new int[]{android.R.id.text1});
                lv.setAdapter(adapter);



                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                            long id) {
                        // Intent intent = new Intent(MyActivity.this, MyActivity15.class);

                        TextView tv=(TextView) itemClicked;
                        String s=tv.getText().toString();

                        //cursor = MyActivity.sqdb.query(DataBase.TABLE_NAME, null, null, null, null, null, null);
                        boolean t=true;
                        cursor.moveToFirst();
                        while(cursor.moveToNext()&&t){
                            String y=cursor.getString(cursor.getColumnIndex(DataBase.POSTNAME));
                            if (y!=null&&y.equals(s)) {
                                s = cursor.getString(cursor.getColumnIndex(DataBase.EMAIL));
                                t=false;
                            }
                        }
                        //String name = cursor1.getString(cursor1.getColumnIndex(DataBase.EMAIL));



                        final Intent intent = new Intent("SOME_COMMAND_ACTION", null, MyActivity.this, IServise.class);
                        intent.putExtra(Constants.RECEIVER, mReceiver).putExtra("task", s);
                        startService(intent);

                        setContentView(R.layout.fadk);
                        mProgress = (ProgressBar) findViewById(R.id.progressBar);


                        //          startActivity(intent);
                        onDestroy();
                    }
                });





                break;
        }

    }

    protected void onResume() {
        super.onResume();
        mReceiver = new AppReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReceiver.setReceiver(null);
    }


}
