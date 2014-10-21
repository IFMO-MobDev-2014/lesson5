package ru.md.ifmo.lesson5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;



public class MyActivity extends Activity  {

    Button go;
    EditText editText;
    ListView listView;
    RSSTask loader;
    boolean browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        AlertDialog.Builder dialog = new AlertDialog.Builder(MyActivity.this);
        dialog.setTitle("Welcome!");
        dialog.setMessage("Where do you want to show web pages?");
        dialog.setPositiveButton("Browser", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                browser = true;
            }
        });
        dialog.setNegativeButton("This app", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                browser = false;
            }
        });
        dialog.show();
        go = (Button)findViewById(R.id.button2);
        editText = (EditText) findViewById(R.id.editText2);
        listView =(ListView) findViewById(R.id.listView2);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(),"Loading...", Toast.LENGTH_SHORT);
                toast.show();
                parseXML();
            }
        });
    }

    private void parseXML() {
        try {
            loader = new RSSTask();
            loader.execute(editText.getText().toString());
            if (loader.exception != null) {
                throw new Exception();
            }
            ArrayList<ItemMaster> itemsList = loader.get();
            MyAdapter adapter = new MyAdapter(this, itemsList, browser);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Oops, something goes wrong", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
