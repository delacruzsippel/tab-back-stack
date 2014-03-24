package de.appetites.tabbackstack;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by appetites.de on 22.03.2014.
 */
public class FragmentBackStack implements Parcelable {
    Stack<Fragment> mFragments;
    ArrayList<FragmentInfo> fragmentInfos;

    public FragmentBackStack() {
        mFragments = new Stack<Fragment>();
    }

    public int size() {
        return mFragments.size();
    }

    public void push(Fragment fragment, FragmentTransaction fragmentTransaction, int containerId) {
        fragmentTransaction.replace(containerId, fragment);
        if(mFragments.size() == 0 || mFragments.size() > 0 && fragment != mFragments.peek()) {
            mFragments.push(fragment);
        }
    }

    public Fragment pop(FragmentManager fragmentManager) {
        return mFragments.pop();
    }

    public Fragment getCurrent(FragmentManager fragmentManager) {
        return mFragments.peek();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        ArrayList<FragmentInfo> fragmentInfos = new ArrayList<FragmentInfo>();
        for(Fragment fragment : mFragments){
            FragmentInfo fragmentInfo = new FragmentInfo();
            fragmentInfo.className = fragment.getClass().getName();
            fragmentInfo.arguments = fragment.getArguments();
            fragmentInfos.add(fragmentInfo);
        }
        out.writeTypedList(fragmentInfos);
    }

    public static final Creator<FragmentBackStack> CREATOR
            = new Creator<FragmentBackStack>() {
        public FragmentBackStack createFromParcel(Parcel in) {
            return new FragmentBackStack(in);
        }

        public FragmentBackStack[] newArray(int size) {
            return new FragmentBackStack[size];
        }
    };

    private FragmentBackStack(Parcel in) {
        mFragments = new Stack<Fragment>();
        fragmentInfos = new ArrayList<FragmentInfo>();
        in.readTypedList(fragmentInfos,FragmentInfo.CREATOR);
    }

    public void recreateBackStack(Context context){
        if(mFragments.size() == 0 && fragmentInfos != null){
            Log.d("xxx",String.format("fragments = %d, fragmentInfos = %d",mFragments.size(),fragmentInfos.size()));
            for (FragmentInfo fragmentInfo : fragmentInfos){
                Fragment fragment = Fragment.instantiate(context, fragmentInfo.className);
                fragment.setArguments(fragmentInfo.arguments);
                mFragments.push(fragment);
            }
            fragmentInfos.clear();
            fragmentInfos = null;
        }
    }
}
