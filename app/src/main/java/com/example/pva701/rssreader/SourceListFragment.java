package com.example.pva701.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
        /*Bundle bundle = new Bundle();
        bundle.putBoolean(PollService.NOTIFICATION, true);
        bundle.putInt(PollService.POLL_INTERVAL, 1000);
        PollService.setServiceAlarm(getActivity(), true, bundle);*/
        //PollService.setServiceAlarm(getActivity(), false, null);
    }

    private BroadcastReceiver onUpdateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
            SourcesManager.getInstance(getActivity()).resume();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onUpdateBroadcastReceiver, new IntentFilter(PollService.UPDATE_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onUpdateBroadcastReceiver);
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
                    lastUpdate.setText("Last update: " + new SimpleDateFormat("d MMM yyyy, HH:mm").format(source.getLastUpdate()));
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

    private AlertDialog intervalDialog;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_add_source) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AddSourceDialog dialog = new AddSourceDialog();
            dialog.setTargetFragment(SourceListFragment.this, REQUEST_ADD_SOURCE);
            dialog.show(fm, DIALOG_ADD_SOURCE);
            return true;
        } else if (item.getItemId() == R.id.menu_item_auto_update) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Interval");
            final CharSequence[] intervals = {"Never", "1 minute", "5 minute", "15 minute", "30 minute", "1 hour", "3 hour", "6 hour", "12 hour", "1 day"};
            builder.setSingleChoiceItems(intervals, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0)
                        PollService.setServiceAlarm(getActivity(), false, null);
                    else {
                        int j = 0, num = 0;
                        String s = intervals[i].toString();
                        while (Character.isDigit(s.charAt(j))) {
                            num = num * 10 + s.charAt(j) - '0';
                            ++j;
                        }
                        ++j;
                        int mills = 0;
                        if (s.charAt(j) == 'm') mills = num * 60;
                        else if (s.charAt(j) == 'h') mills = num * 3600;
                        else if (s.charAt(j) == 'd') mills = num * 3600 * 24;
                        PollService.setServiceAlarm(getActivity(), false, null);
                        mills *= 1000;
                        Bundle bundle = new Bundle();
                        bundle.putInt(PollService.POLL_INTERVAL, mills);
                        bundle.putBoolean(PollService.NOTIFICATION, false);
                        PollService.setServiceAlarm(getActivity(), true, bundle);
                    }
                    intervalDialog.dismiss();
                }
            });
            intervalDialog = builder.create();
            intervalDialog.show();
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
                sources.add(cur);
                adapter.notifyDataSetChanged();
                SourcesManager.getInstance(getActivity()).resume();
                //getActivity().startService(new Intent(getActivity(), PollService.class).putExtra(PollService.TARGET_SOURCE_ID, cur.getId()).
                        //putExtra(PollService.TARGET_URL, cur.getUrl()));
            }
        } else {

        }
    }
}
