package ru.md.ifmo.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MyActivity extends Activity implements AdapterView.OnItemClickListener {

    Button go;
    EditText editText;
    ListView listView;
    RSSTask loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        go = (Button)findViewById(R.id.button2);
        editText = (EditText) findViewById(R.id.editText2);
        listView =(ListView) findViewById(R.id.listView2);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseXML();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                            long id) {
        WebView link = (WebView) itemClicked.findViewById(R.id.link);
        TextView linkHelper = (TextView) itemClicked.findViewById(R.id.link_helper);
        link.setWebViewClient(new MyWebViewClient());
        link.loadUrl(linkHelper.getText().toString());
    }

    private void parseXML() {

        try {
            loader = new RSSTask();
            loader.execute(editText.getText().toString());
            if (loader.exception != null) {
                Toast toast = Toast.makeText(getApplicationContext(), "fuck you", Toast.LENGTH_SHORT);
                toast.show();
            }

            ArrayList<ItemMaster> itemsList = loader.get();
            MyAdapter adapter = new MyAdapter(this, itemsList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);

        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Oops, something goes wrong", Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }


}
