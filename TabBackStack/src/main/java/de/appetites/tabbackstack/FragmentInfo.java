package de.appetites.tabbackstack;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by appetites.de on 24.03.2014.
 */
public class FragmentInfo implements Parcelable {
    public String className;
    public Bundle arguments;

    public static final Creator<FragmentInfo> CREATOR
            = new Creator<FragmentInfo>() {
        public FragmentInfo createFromParcel(Parcel in) {
            return new FragmentInfo(in);
        }

        public FragmentInfo[] newArray(int size) {
            return new FragmentInfo[size];
        }
    };

    public FragmentInfo(Parcel in) {
        className = in.readString();
        arguments = in.readBundle();
    }

    public FragmentInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(className);
        out.writeBundle(arguments);
    }
}
