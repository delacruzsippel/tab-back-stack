package de.appetites.tabfragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.appetites.tabbackstack.FragmentBackStack;
import de.appetites.tabbackstack.TabBackStackActivity;

import java.util.UUID;


public class MainActivity extends TabBackStackActivity{

    private static final String TAG = "TabFragments";
    protected ActionBar mActionBar;
    SparseArray<FragmentBackStack> mTabBackStacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = getActionBar();
        // create tabs
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.addTab(mActionBar.newTab().setText("Tab 1").setTabListener(this));
        mActionBar.addTab(mActionBar.newTab().setText("Tab 2").setTabListener(this));
        mActionBar.addTab(mActionBar.newTab().setText("Tab 3").setTabListener(this));
        mActionBar.addTab(mActionBar.newTab().setText("Tab 4").setTabListener(this));
    }

    @Override
    public int getContainerId() {
        return R.id.container;
    }


    @Override
    protected Fragment initTab(int position) {
        Fragment fragment = new DummySectionFragment();
        Bundle args = new Bundle();
        args.putString(DummySectionFragment.ARG_SECTION_NUMBER, UUID.randomUUID().toString());
        fragment.setArguments(args);
        return fragment;
    }

    public static class DummySectionFragment extends Fragment {
        public static final String ARG_SECTION_NUMBER = "placeholder_text";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);

            // text view with info about current fragment
            // displays : {tabposition}-{size_of_current_tabstack}
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            textView.setText(getArguments().getString(ARG_SECTION_NUMBER));
            layout.addView(textView);


            // creat button for pushing new dummy fragment
            Button button = new Button(getActivity());
            button.setText("Push");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putString(DummySectionFragment.ARG_SECTION_NUMBER, UUID.randomUUID().toString());
                    fragment.setArguments(args);
                    ((MainActivity)getActivity()).push(fragment);
                }
            });
            layout.addView(button);

            return layout;
        }
    }
}
