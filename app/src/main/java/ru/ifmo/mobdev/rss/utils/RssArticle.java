package ru.ifmo.mobdev.rss.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.ifmo.mobdev.rss.LoaderActivity;
import ru.ifmo.mobdev.rss.R;

/**
 * @author sugakandrey
 */
public class RssArticle {

    private String title;
    private String description;
    private String date;
    private String url;
    private boolean expanded;
    private FeedItem feedItem;
    private Bitmap thumbnail;

    public void setThumbnailUrl(String thumbnailUrl) {
        new LoadThumbnailTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, thumbnailUrl);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public View inflate(View view, LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.rss_item, null);
            feedItem = new FeedItem();
            feedItem.title = (TextView) view.findViewById(R.id.rss_title);
            feedItem.description = (TextView) view.findViewById(R.id.rss_data);
            view.setTag(feedItem);
            feedItem.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((RssArticle) view.getTag()).expand();
                }
            });
        } else {
            feedItem = (FeedItem) view.getTag();
        }
        TextView title = feedItem.getTitle();
        title.setText(this.title);
        title.setTag(this);
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        if (date != null) {
            try {
                Date date = inputFormat.parse(this.date);
                title.append("\n" + outputFormat.format(date));
            } catch (ParseException e) {
                Log.e("Error parsing date", e.getMessage());
            }
        }
        ((ImageView) view.findViewById(R.id.thumbnail_image)).setImageBitmap(thumbnail);
        TextView data = feedItem.getDescription();
        data.setText(Html.fromHtml(description));
        data.setVisibility(expanded ? View.VISIBLE : View.GONE);
        return view;
    }


    public void expand() {
        expanded = !expanded;
        feedItem.description.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    public void onThumbnailLoaded(Bitmap bitmap) {
        thumbnail = bitmap;
        LoaderActivity.adapter.notifyDataSetChanged();
    }

    private class FeedItem {
        TextView title;
        TextView description;

        public TextView getTitle() {
            return title;
        }

        public TextView getDescription() {
            return description;
        }
    }

}
