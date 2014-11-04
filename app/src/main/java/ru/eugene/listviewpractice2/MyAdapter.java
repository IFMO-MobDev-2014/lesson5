package ru.eugene.listviewpractice2;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by eugene on 10/28/14.
 */
public class MyAdapter extends ArrayAdapter<String> {
    private static final String LOG = "DEBUG";
    List<String> data;
    int resource;
    Context context;
    int [] imagesId;

    public MyAdapter(Context context, int resource, List<String> data) {
        super(context, resource, data);
        this.data = data;
        this.resource = resource;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.i(LOG, "current position: " + position);
//        Log.i(LOG, "convertView is null: " + ((convertView == null) ? "true" : "false"));
        Holder holder;
        View row = convertView;
        if (row == null) {
            LayoutInflater myInflater = ((Activity) context).getLayoutInflater();
            row = myInflater.inflate(resource, parent, false);
            holder = new Holder();
            holder.txtView = (TextView) row.findViewById(R.id.text);
//            holder.imgView = (ImageView) row.findViewById(R.id.img);

            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        holder.txtView.setText(Html.fromHtml(data.get(position)));
//        holder.imgView.setImageResource(imagesId[position & (4 - 1)]);

        return row;
    }

    class Holder {
        ImageView imgView;
        TextView txtView;
    }
}