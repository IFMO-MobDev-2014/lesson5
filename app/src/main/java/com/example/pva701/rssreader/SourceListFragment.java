package com.example.pva701.rssreader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.pva701.rssreader.SourcesManager.Source;

/**
 * Created by pva701 on 14.10.14.
 */
public class SourceListFragment extends Fragment {
    private static final int REQUEST_ADD_SOURCE = 1;
    private static final String DIALOG_ADD_SOURCE = "DIALOG_ADD_SOURCE";
    private ArrayList <Source> sources;
    private ListView listSourceItems;
    private ArrayAdapter <Source> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sources = SourcesManager.getInstance(getActivity()).getSources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_source_list, container, false);
        listSourceItems = (ListView)v.findViewById(R.id.listSourceIems);

        adapter = new ArrayAdapter<Source>(getActivity(), R.layout.source_list_item, sources) {
            @Override
            public int getCount() {
                return sources.size();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.source_list_item, null);
                TextView sourceName = (TextView)convertView.findViewById(R.id.source_name);
                TextView url = (TextView)convertView.findViewById(R.id.source);
                TextView lastUpdate = (TextView)convertView.findViewById(R.id.last_update);
                Source source = getItem(position);
                sourceName.setText(source.getName());
                if (source.getLastUpdate().getTime() != 0)
                    lastUpdate.setText("Last update: " + new SimpleDateFormat("dd.MM.yyyy hh:mm").format(source.getLastUpdate()));
                else
                    lastUpdate.setText("");
                url.setText(source.getUrl());
                return convertView;
            }
        };

        listSourceItems.setAdapter(adapter);
        listSourceItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), NewsListPagerActivity.class);
                intent.putExtra(NewsListPagerActivity.NAME_ID, i);
                startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_source_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_source) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AddSourceDialog dialog = new AddSourceDialog();
            dialog.setTargetFragment(SourceListFragment.this, REQUEST_ADD_SOURCE);
            dialog.show(fm, DIALOG_ADD_SOURCE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_SOURCE) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(AddSourceDialog.EXTRA_NAME);
                String source = data.getStringExtra(AddSourceDialog.EXTRA_SOURCE);
                if (!source.startsWith("http://"))
                    source = "http://" + source;
                final Source cur = new Source(name, source, new Date(0));
                sources.add(cur);//TODO change
                adapter.notifyDataSetChanged();
                SourcesManager.getInstance(getActivity()).resume();

                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        NewsManager.getInstance(getActivity()).mergeNews(RSSFetcher.fetch(cur.getUrl()), cur.getId());
                        return true;
                    }
                };//TODO execute
            }
        } else {

        }
    }
}
