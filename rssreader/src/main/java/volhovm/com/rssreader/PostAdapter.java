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

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author volhovm
 *         Created on 10/21/14
 */

public class PostAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<LoadRSSTask.Item> items;
    private HashMap<URL, Bitmap> imageCache;

    public PostAdapter(Activity context, ArrayList<LoadRSSTask.Item> items, String[] list) {
        super(context, R.layout.post_layout, list);
        this.context = context;
        this.items = items;
        imageCache = new HashMap<URL, Bitmap>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LoadRSSTask.Item item = items.get(position);
        final View postView;
        final TextView title, description, date;
        ImageView imageView;
        if (item.enclosure != null) {
            postView = inflater.inflate(R.layout.post_layout, parent, false);
            imageView = (ImageView) postView.findViewById(R.id.post_image);
            new ImageLoadTask(imageView, imageCache).execute(item.enclosure);
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
            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url.toString());
            try {

                HttpResponse response = client.execute(getRequest);

                //check 200 OK for success
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
                        // getting contents from the stream
                        inputStream = entity.getContent();

                        // decoding stream data back into image Bitmap that android understands
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
                // You Could provide a more explicit error message for IOException
                getRequest.abort();
                Log.e("ImageDownloader", "Something went wrong while" +
                        " retrieving bitmap from " + url + e.toString());
            }

            return null;
        }
    }
}
