package com.damon.kill.alive.keeplive.bean;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

public class DamonParcel implements Parcelable {
    public String[] strArr;
    public String str;

    public Intent intent;
    public Intent intent2;
    public Intent intent3;

    public static final Creator<DamonParcel> CREATOR = new Creator<DamonParcel>() {
        @Override
        public DamonParcel createFromParcel(Parcel parcel) {
            return new DamonParcel(parcel);
        }

        @Override
        public DamonParcel[] newArray(int i) {
            return new DamonParcel[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public DamonParcel() {
    }

    protected DamonParcel(Parcel parcel) {
        strArr = parcel.createStringArray();
        str = parcel.readString();
        if (parcel.readInt() != 0) {
            intent = Intent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            intent2 = Intent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            intent3 = Intent.CREATOR.createFromParcel(parcel);
        }
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(strArr);
        parcel.writeString(str);
        if (intent == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            intent.writeToParcel(parcel, i);
        }
        if (intent2 == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            intent2.writeToParcel(parcel, i);
        }
        if (intent3 == null) {
            parcel.writeInt(0);
            return;
        }
        parcel.writeInt(1);
        intent3.writeToParcel(parcel, i);
    }

    public static DamonParcel create(String str) {
        byte[] decode = Base64.decode(str, 2);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(decode, 0, decode.length);
        obtain.setDataPosition(0);
        return CREATOR.createFromParcel(obtain);
    }

    @Override
    public String toString() {
        Parcel obtain = Parcel.obtain();
        writeToParcel(obtain, 0);
        String encodeToString = Base64.encodeToString(obtain.marshall(), 2);
        obtain.recycle();
        return encodeToString;
    }
}

