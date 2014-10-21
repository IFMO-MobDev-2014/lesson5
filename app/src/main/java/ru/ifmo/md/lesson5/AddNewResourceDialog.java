package ru.ifmo.md.lesson5;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Svet on 19.10.2014.
 */
public class AddNewResourceDialog extends Dialog {
    EditText name, url;
    DataStorage ds;
    ListAdapter listAdapter;
    ListView lv;

    public AddNewResourceDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_add_rss);
        setTitle("Add new URL resource");
        name = (EditText) findViewById(R.id.rss_name);
        url = (EditText) findViewById(R.id.rss_url);
    }

    public void initFields(final DataStorage ds, final ListAdapter listAdapter, final ListView lv) {
        this.ds = ds;
        this.listAdapter = listAdapter;
        this.lv = lv;

        addKeyListeners();
        onAddButton();
    }

    private void addKeyListeners() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        name.requestFocus();

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_SEND) {
                    url.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if(i == EditorInfo.IME_ACTION_SEND) {
                    updateDatabaseAndListView();
                    dismiss();
                    handled = true;
                }
                return handled;
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
        ds.updateInformation();
        listAdapter.notifyDataSetChanged();
        lv.setAdapter(listAdapter);
    }
}
