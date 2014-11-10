package volhovm.com.rssreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import volhovm.com.rssreader.Feed.Item;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * @author volhovm
 *         Created on 10/21/14
 */

public class PostAdapter extends ArrayAdapter<Item> {
    private final Activity context;
    private Feed feed;
    private HashMap<URL, Bitmap> imageCache;

    public void replaceFeed(Feed feed) {
        this.feed = new Feed(feed);
        final PostAdapter postAdapter = this;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    public PostAdapter(Activity context, Feed feed) {
        super(context, R.layout.fragment_rssmain, feed.getItems());


        this.context = context;
        this.feed = new Feed(feed);
        imageCache = new HashMap<URL, Bitmap>();
        final PostAdapter postAdapter = this;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyDataSetInvalidated();
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return feed == null ? 0 : feed.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Item item = feed.get(position);
        final View postView;
        final TextView title, description, date;
        ImageView imageView;
        if (item.pictureLink != null) {
            postView = inflater.inflate(R.layout.post_layout, parent, false);
            imageView = (ImageView) postView.findViewById(R.id.post_image);
            new ImageLoadTask(imageView, imageCache).execute(item.pictureLink);
            title = (TextView) postView.findViewById(R.id.post_header);
            description = (TextView) postView.findViewById(R.id.post_description);
            date = (TextView) postView.findViewById(R.id.post_date);
        } else {
            postView = inflater.inflate(R.layout.post_no_image_layout, parent, false);
            title = (TextView) postView.findViewById(R.id.post_noim_header);
            description = (TextView) postView.findViewById(R.id.post_noim_description);
            date = (TextView) postView.findViewById(R.id.post_noim_date);
        }
        postView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(item.link)));
                context.startActivity(browserIntent);
            }
        });
        title.setText(item.title);
        description.setText(item.description);
        date.setText(new SimpleDateFormat("HH:mm dd.MM.yyyy").format(item.date));
        return postView;
    }

    private class ImageLoadTask extends AsyncTask<URL, Void, Bitmap> {
        private final ImageView imageView;
        private final HashMap<URL, Bitmap> cache;

        public ImageLoadTask(ImageView imageView, HashMap<URL, Bitmap> cache) {
            this.imageView = imageView;
            this.cache = cache;
        }

        @Override
        protected void onPreExecute() {
            Log.i("Async-Example", "onPreExecute Called");
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            Log.i("Async-Example", "onPostExecute Called");
            imageView.setImageBitmap(result);
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            if (cache.containsKey(url)) return cache.get(url);
            final DefaultHttpClient client = new DefaultHttpClient();

            final HttpGet getRequest = new HttpGet(url.toString());
            try {
                HttpResponse response = client.execute(getRequest);
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    Log.w("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;
                }
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = entity.getContent();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        cache.put(url, bitmap);
                        return bitmap;
                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }
            return null;
        }
    }
}
