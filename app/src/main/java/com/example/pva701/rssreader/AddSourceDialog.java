package com.example.pva701.rssreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by pva701 on 18.10.14.
 */
public class AddSourceDialog extends DialogFragment {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_SOURCE = "source";

    private void sendResult(String name, String source) {
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_SOURCE, source);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("New source");
        final View v = inflater.inflate(R.layout.dialog_add_source, null);
        builder.setView(v)
                .setPositiveButton(R.string.add_source, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(((EditText)v.findViewById(R.id.dialog_source_name)).getText().toString(),
                                ((EditText)v.findViewById(R.id.dialog_source)).getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddSourceDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
