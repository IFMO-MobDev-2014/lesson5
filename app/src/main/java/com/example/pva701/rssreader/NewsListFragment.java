package com.example.pva701.rssreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by pva701 on 18.10.14.
 */
public class NewsListFragment extends Fragment {
    private static final String SOURCE_ID = "id_in_array";
    public static Fragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(SOURCE_ID, id);
        Fragment f = new NewsListFragment();
        f.setArguments(bundle);
        return f;
    }

    private ArrayAdapter<NewsManager.News> adapter;
    private SourcesManager.Source source;
    private ArrayList <NewsManager.News> forAdapter;
    private SwipeRefreshLayout refreshLayout;
    private BroadcastReceiver onUpdateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshLayout.setRefreshing(true);
            ArrayList <NewsManager.News> result = NewsManager.getInstance(getActivity()).getNewses(source.getId());
            forAdapter = result;
            adapter.clear();
            for (int i = 0; i < result.size(); ++i)
                adapter.add(result.get(i));
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
            shitAlreadyRefreshed = false;
            getActivity().startService(new Intent(getActivity(), SaveDataService.class));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onUpdateBroadcastReceiver, new IntentFilter(PollService.UPDATE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onUpdateBroadcastReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        source = SourcesManager.getInstance(getActivity()).getSources().get(getArguments().getInt(SOURCE_ID));
    }

    private boolean shitAlreadyRefreshed = false;
    private final SimpleDateFormat NEWS_DATE_FORMAT = new SimpleDateFormat("d MMM yyyy, HH:mm");
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_news_list, container, false);
        //final LayoutInflater myInflater = LayoutInflater.from(getActivity());
        refreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh);
        refreshLayout.setColorScheme(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);//TODO change colors

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnline()) {
                    Toast.makeText(getActivity(),
                            "Check your internet connection", Toast.LENGTH_SHORT).show();
                    refreshLayout.setRefreshing(false);
                    return;
                }
                if (shitAlreadyRefreshed)
                    return;
                shitAlreadyRefreshed = true;
                getActivity().startService(new Intent(getActivity(), PollService.class).putExtra(PollService.TARGET_SOURCE_ID, source.getId()).
                        putExtra(PollService.TARGET_URL, source.getUrl()));
            }
        });

        forAdapter = NewsManager.getInstance(getActivity()).getNewses(source.getId());
        ListView listView = (ListView)v.findViewById(R.id.listNews);
        adapter = new ArrayAdapter<NewsManager.News>(getActivity(), R.layout.news_list_item, forAdapter) {
            @Override
            public int getCount() {
                return forAdapter.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                NewsManager.News news = getItem(position);
                if (convertView == null)
                    convertView = View.inflate(getActivity(), R.layout.news_list_item, null);
                TextView title = (TextView)convertView.findViewById(R.id.title);
                TextView pubDate = (TextView)convertView.findViewById(R.id.pub_date);
                if (!news.isRead())
                    title.setTypeface(null, Typeface.BOLD);
                else
                    title.setTypeface(null, 0);
                title.setText(news.getTitle());
                pubDate.setText("Published: " + NEWS_DATE_FORMAT.format(news.getPubDate()));

                //TextView description = (TextView)convertView.findViewById(R.id.description);
                //description.setText(Html.fromHtml(news.getDescription()));
                return convertView;
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isOnline()) {
                    Toast.makeText(getActivity(),
                            "Check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), ShowWebView.class);
                NewsManager.News cur = adapter.getItem(i);
                intent.putExtra(ShowWebView.URL, cur.getLink());
                startActivity(intent);
                cur.setRead(true);
                getActivity().startService(new Intent(getActivity(), SaveDataService.class));
                adapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);
        return v;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
