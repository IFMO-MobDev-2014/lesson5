package odeen.rssreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Женя on 17.10.2014.
 */
public class NewsListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NewsListFragment();
    }
}
