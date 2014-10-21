package ru.ifmo.md.lesson5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import android.text.Html;
import android.widget.EditText;

public class MyActivity extends Activity {
    ListView listview;
    EditText editText;
    ProgressDialog progress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        listview = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.EditText);
        if (RSS.n != null) {
            createListView();
        }
       // editText.setText(Html.fromHtml("<font color='#FF0000'>http://feeds.bbci.co.uk/news/rss.xml</font> "));
        editText.setHint("http://stackoverflow.com/feeds/tag/android");

    }
    public void createListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RSS.n);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(MyActivity.this, Web.class);
                intent.putExtra("text", RSS.t[position]);
                startActivity(intent);
                finish();
            }
        });

    }
    private class RSSLoad extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            String xml = getXmlFromUrl(url);
            Document doc = null;
            Boolean error;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                ByteArrayInputStream encXML = new ByteArrayInputStream(xml.getBytes("utf-8"));
                doc = builder.parse(encXML);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (doc != null) {
                RSS.parse(doc);
                error = false;
            } else {
                error = true;
            }
            return error;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progress = ProgressDialog.show(MyActivity.this, "", "Wait,please...");
        }
        public String getXmlFromUrl(String url) {
            String xml = null;
             try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                xml = EntityUtils.toString(httpEntity);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return xml;
        }

        @Override
        protected void onPostExecute(Boolean error) {
            progress.dismiss();
            if (error) {
                editText.setText("");
                editText.setHint("invalid url");
            } else {
                createListView();
            }
        }


    }
    public void parseRss(View v) {
        String hint = editText.getHint().toString();
        editText.setHint("");
        String url;
        String tex = editText.getText().toString();
        if (hint.length() == 0 || tex.length() != 0) {
            url = tex;
        } else {
            url = hint;
        }
        new RSSLoad().execute(url);
    }
}
