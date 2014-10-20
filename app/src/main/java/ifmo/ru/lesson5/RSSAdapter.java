package ifmo.ru.lesson5;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mariashka on 10/19/14.
 */
public class RSSAdapter extends BaseAdapter {
    public List <RSSItem> data;

    public RSSAdapter(List<RSSItem> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View l = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item, parent, false);

        RSSItem item = (RSSItem) getItem(position);

        TextView title = (TextView) l.findViewById(R.id.textView);
        title.setText(item.getTitle());

        TextView text = (TextView) l.findViewById(R.id.textView2);
        String styledText = Html.fromHtml(item.getText()).toString();
        text.setText(styledText);

        return l;
    }
}
