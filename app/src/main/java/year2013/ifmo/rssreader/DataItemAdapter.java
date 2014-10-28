package year2013.ifmo.rssreader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Юлия on 25.10.2014.
 */
public class DataItemAdapter extends ArrayAdapter<Data> {
    private Activity myActiv;
    private ArrayList<Data> datas;

    public DataItemAdapter(Context context, int textViewResourceId,
                           ArrayList<Data> objects) {
        super(context, textViewResourceId, objects);
        myActiv = (Activity) context;
        datas = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View smallView;

        if(convertView == null) {
            LayoutInflater inflater = myActiv.getLayoutInflater();
            convertView = inflater.inflate(R.layout.postitem, null);
        }

        smallView = convertView;
        ImageView thumbImageView = (ImageView) smallView
                .findViewById(R.id.postThumb);
        if (datas.get(position).aThumbUrl == null) {
            thumbImageView.setImageResource(R.drawable.pop);
        }

        TextView postTitleView = (TextView) smallView
                .findViewById(R.id.postTitleLabel);
        postTitleView.setText(datas.get(position).aTitle);

        return smallView;
    }
}
