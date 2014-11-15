package default_.lesson5.auxillary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import default_.lesson5.R;

/**
 * Created by default on 21.10.14.
 */
public class EntryAdapter extends BaseAdapter {
    Context context;
    Feed feed;
    LayoutInflater inflater;

    public EntryAdapter(Context context, Feed feed) {
        this.context = context;
        this.feed = feed;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return feed.countEntries();
    }

    @Override
    public Object getItem(int position) {
        if (0 <= position && position < feed.countEntries()) {
            return feed.getEntry(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.entry, parent, false);
        }
        Entry entry = feed.getEntry(position);

        TextView title = (TextView) view.findViewById(R.id.entry_title);
        title.setText(entry.getTitle());

        return view;
    }


}