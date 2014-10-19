package odeen.rssreader;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.UUID;

/**
 * Created by Женя on 18.10.2014.
 */
public class NewsWebViewFragment extends Fragment {

    private News mNews;

    public static Fragment newInstance(UUID id) {
        NewsWebViewFragment fragment = new NewsWebViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.BUNDLE_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNews = RSSNewsLibrary.get(getActivity()).get((UUID)getArguments().getSerializable(Constants.BUNDLE_ID));
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null)
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        View v = inflater.inflate(R.layout.news_fragment, container, false);
        WebView webView = (WebView)v.findViewById(R.id.news_webView);
        webView.loadUrl(mNews.getURL());
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        getActivity().getActionBar().setTitle(mNews.getTitle());
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return false;
        }
    }

}
