package volhovm.com.rssreader;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author volhovm
 *         Created on 10/21/14
 */

public class PostAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<LoadRSSTask.Item> items;

    public PostAdapter(Activity context, ArrayList<LoadRSSTask.Item> items, String[] list) {
        super(context, R.layout.post_layout, list);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LoadRSSTask.Item item = items.get(position);
        View postView;
        TextView title, description;
        ImageView imageView;
        if (item.enclosure != null) {
            postView = inflater.inflate(R.layout.post_layout, parent, false);
            imageView = (ImageView) postView.findViewById(R.id.post_image);
            imageView.setImageURI(Uri.parse(item.enclosure.toString()));
            title = (TextView) postView.findViewById(R.id.post_header);
            description = (TextView) postView.findViewById(R.id.post_description);
        } else {
            postView = inflater.inflate(R.layout.post_no_image_layout, parent, false);
            title = (TextView) postView.findViewById(R.id.post_noim_header);
            description = (TextView) postView.findViewById(R.id.post_noim_description);
        }
        title.setText(item.title);
        description.setText(item.description);
        return postView;
    }
}
