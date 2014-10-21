package odeen.rssreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by Женя on 17.10.2014.
 */
public class NewsListActivity extends SingleFragmentActivity {
    public static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected Fragment createFragment() {
        int table_id = getIntent().getIntExtra(CHANNEL_ID, -1);
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CHANNEL_ID, table_id);
        fragment.setArguments(args);
        return fragment;
    }
}
