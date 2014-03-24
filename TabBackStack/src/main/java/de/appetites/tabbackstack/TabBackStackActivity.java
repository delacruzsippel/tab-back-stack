package de.appetites.tabbackstack;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;


public abstract class TabBackStackActivity extends Activity implements ActionBar.TabListener {

    private static final String TAG = "TabFragments";
    protected ActionBar mActionBar;
    SparseArray<FragmentBackStack> mTabBackStacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTabBackStacks = savedInstanceState.getSparseParcelableArray("TAB_STACKS");
            for (int i = 0; i < mTabBackStacks.size(); i++) {
                mTabBackStacks.get(i).recreateBackStack(this);
            }
        } else if (mTabBackStacks == null) {
            mTabBackStacks = new SparseArray<FragmentBackStack>();
            for (int i = 0; i < 4; i++) {
                FragmentBackStack fragmentBackStack = new FragmentBackStack();
                mTabBackStacks.append(i, fragmentBackStack);
            }
        }
        mActionBar = getActionBar();
//        // create tabs
//        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        mActionBar.addTab(mActionBar.newTab().setText("Tab 1").setTabListener(this));
//        mActionBar.addTab(mActionBar.newTab().setText("Tab 2").setTabListener(this));
//        mActionBar.addTab(mActionBar.newTab().setText("Tab 3").setTabListener(this));
//        mActionBar.addTab(mActionBar.newTab().setText("Tab 4").setTabListener(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSparseParcelableArray("TAB_STACKS", mTabBackStacks);
        super.onSaveInstanceState(outState);
    }

    /**
     * Pushes a fragment onto the currents tab backstack and displays it
     *
     * @param fragment Fragment to push and display
     */
    public void push(Fragment fragment) {
        int position = mActionBar.getSelectedTab().getPosition();
        FragmentBackStack fragmentBackStack = mTabBackStacks.get(position);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentBackStack.push(fragment, fragmentTransaction, getContainerId());
        fragmentTransaction.commit();
    }

    public abstract int getContainerId();

    /**
     * * Pops the top most fragment from the current tab, removes it from the backstack
     * and displays the last fragment in the changed backstack
     *
     * @return true if the fragment could be popped, false if the backstack only has one element left
     */
    public boolean pop() {
        int position = mActionBar.getSelectedTab().getPosition();
        FragmentBackStack fragmentBackStack = mTabBackStacks.get(position);

        if (fragmentBackStack.size() > 1) {
            fragmentBackStack.pop(getFragmentManager());

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(getContainerId(), fragmentBackStack.getCurrent(getFragmentManager()));
            fragmentTransaction.commit();
            return true;
        } else {
            return false;
        }

    }

    /**
     * Pops the current fragment of the curren tab if back is pressed
     */
    @Override
    public void onBackPressed() {
        if (!pop()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        Fragment fragment = null;

        // get current tab position and retrieve the backstack
        int position = tab.getPosition();
        FragmentBackStack fragmentBackStack = mTabBackStacks.get(position);

        // if backstack empty init a dummy fragment else get last fragment of back stack
        if (fragmentBackStack.size() == 0) {
            // init tab
            fragment = initTab(position);
        } else {
            // display last fragment of backstack
            fragment = fragmentBackStack.getCurrent(getFragmentManager());
        }

        // replace current fragment with the new fragment to display
        if (fragment != null) {
            fragmentBackStack.push(fragment, fragmentTransaction, getContainerId());
        } else {
            Log.e(TAG, "Error replacing fragment", new RuntimeException("Could not replace fragment, new fragment must not be null"));
        }
    }

    protected abstract Fragment initTab(int position);

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
