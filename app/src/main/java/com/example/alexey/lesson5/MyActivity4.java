package com.example.alexey.lesson5;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MyActivity4 extends Activity {

    DataBase sqh;
    SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity4);

        // Инициализируем наш класс-обёртку
        sqh = new DataBase(this);

        // База нам нужна для записи и чтения
        sqdb = sqh.getWritableDatabase();

    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        // закрываем соединения с базой данных
        sqdb.close();
        sqh.close();
    }


}