package volhovm.com.rssreader;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author volhovm
 *         Created on 11/6/14
 */

public class PlaceholderFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    ListView listView;

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rssmain, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(((RSSMainActivity) getActivity()).adapter);
        ((RSSMainActivity) getActivity()).adapter.notifyDataSetChanged();
        return rootView;
    }

    // change bar title
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((RSSMainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

