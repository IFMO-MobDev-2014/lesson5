package homework5.rssreader;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Anstanasia on 14.10.2014.
 */

public class MyAdapter extends BaseAdapter {
    private ArrayList <TNews> data = new ArrayList<TNews>();

    public void setData(ArrayList<TNews> d) {
        Log.d("adapter", d.get(0).getDescription());
        this.data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TNews getItem(int pos) {
        return data.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Log.d("MyAdapter", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!qwerty");

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        }

        final TNews x = getItem(pos);

        ((TextView) view.findViewById(R.id.title)).setText(x.getTitle());
        ((TextView) view.findViewById(R.id.content)).setText(x.getDescription());
        view.findViewById(R.id.title).setBackgroundColor(Color.GRAY);
        view.findViewById(R.id.content).setBackgroundColor(Color.GRAY);

        return view;
    }
}