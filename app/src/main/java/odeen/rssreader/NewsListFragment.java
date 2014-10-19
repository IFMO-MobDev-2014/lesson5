package odeen.rssreader;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Женя on 18.10.2014.
 */
public class NewsListFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra(Constants.BROADCAST_ACTION).equals(Constants.BROADCAST_ACTION_PROCESS)) {
                    //News news = IntentShell.get(intent);
                    RSSNewsLibrary.get(getActivity()).takeFromPipe();
                    ((NewsAdapter) getListAdapter()).notifyDataSetChanged();
                }
                if (intent.getStringExtra(Constants.BROADCAST_ACTION).equals(Constants.BROADCAST_ACTION_INTERNET_PROBLEM)) {
                    Toast.makeText(getActivity(), "Some problems with internet", Toast.LENGTH_SHORT).show();
                    stopRefresh();
                }
                if (intent.getStringExtra(Constants.BROADCAST_ACTION).equals(Constants.BROADCAST_ACTION_FINISHED)) {
                    Log.d("FINISH","");
                    stopRefresh();
                }
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(Constants.BROADCAST_ACTION));
        NewsAdapter newsAdapter = new NewsAdapter(RSSNewsLibrary.get(getActivity()).getNews());
        setListAdapter(newsAdapter);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        News news = ((NewsAdapter)getListAdapter()).getItem(position);
        Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
        intent.putExtra(Constants.BUNDLE_ID, news.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news_fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_refresh_news:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class NewsAdapter extends ArrayAdapter<News> {
        public NewsAdapter(ArrayList<News> news) {
            super(getActivity(), 0, news);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_news, null);
            }
            News news = getItem(position);
            TextView title = (TextView)convertView.findViewById(R.id.news_list_item_titleTextView);
            if (news.getTitle() != null)
                title.setText(news.getTitle());
            TextView description = (TextView)convertView.findViewById(R.id.news_list_item_descriptionTextView);
            if (news.getDescription() != null)
                description.setText(news.getDescription());
            ImageView imageView = (ImageView)convertView.findViewById(R.id.news_list_item_imageView);
            if (news.getImage() != null)
                imageView.setImageBitmap(news.getImage());
            return convertView;
        }
    }
    private ProgressDialog progressDialog;
    private void refresh() {
        RSSNewsLibrary.get(getActivity()).refresh();
        //progressDialog = ProgressDialog.show(getActivity(), "Wait", "loading data...", true);
    }
    private void stopRefresh() {
        //progressDialog.dismiss();
    }


}
