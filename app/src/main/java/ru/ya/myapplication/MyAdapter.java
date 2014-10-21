package ru.ya.myapplication;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vanya on 21.10.14.
 */
public class MyAdapter extends BaseAdapter {
    ArrayList < OnePost > arrayList = new ArrayList<OnePost>();
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public OnePost getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View _view, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.onepost, viewGroup, false);
        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        textView1.setTypeface(null, Typeface.BOLD);
        textView1.setText(arrayList.get(i).title);

        TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        textView2.setText(arrayList.get(i).date);

        TextView textView3 = (TextView)view.findViewById(R.id.textView3);
        textView3.setText(arrayList.get(i).description);
        return view;
    }
}
