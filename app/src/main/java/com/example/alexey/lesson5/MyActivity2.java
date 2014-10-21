package com.example.alexey.lesson5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MyActivity2 extends Activity {

    public static int g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity2);
        g=getIntent().getExtras().getInt("count");
        TextView textView=(TextView) findViewById(R.id.textViewd);
        textView.setText(MyActivity.b.elementAt(g-1));


    }
    public void goToLink(View view){
        Intent intent=new Intent(this,MyActivity3.class);
        intent.putExtra("count",g);
        startActivity(intent);
    }




}
