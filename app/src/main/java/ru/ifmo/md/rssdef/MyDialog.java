package ru.ifmo.md.rssdef;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by Svet on 19.10.2014.
 */
public class MyDialog extends Dialog {
    EditText name, url;
    DataStorage ds;
    ListAdapter listAdapter;
    ListView lv;

    public MyDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_add_rss);
        setTitle("Add new URL resource");
        name = (EditText) findViewById(R.id.rss_name);
        url = (EditText) findViewById(R.id.rss_url);
    }

    public void initFields(DataStorage ds, ListAdapter listAdapter, ListView lv) {
        this.ds = ds;
        this.listAdapter = listAdapter;
        this.lv = lv;

        addKeyListeners();
        onAddButton();
    }

    private void addKeyListeners() {
        name.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        name.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == 0) {
                    name.clearFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    url.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
                return true;
            }
        });

        url.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == 0) {
                    updateDatabaseAndListView();
                    dismiss();
                }
                return false;
            }
        });
    }

    private void onAddButton() {
        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDatabaseAndListView();
                dismiss();
            }
        });
    }

    private void updateDatabaseAndListView() {
        String rssName = name.getText().toString();
        String rssUrl = url.getText().toString();
        ds.putSourceData(rssName, rssUrl);
        listAdapter.notifyDataSetChanged();
        lv.setAdapter(listAdapter);
    }
}
