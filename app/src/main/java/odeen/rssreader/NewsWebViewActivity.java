package odeen.rssreader;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Женя on 18.10.2014.
 */
public class NewsWebViewActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID id = (UUID)(getIntent().getSerializableExtra(Constants.BUNDLE_ID));
        int table_id = (getIntent().getIntExtra(NewsListActivity.CHANNEL_ID, -1));
        return NewsWebViewFragment.newInstance(id, table_id);
    }
}
