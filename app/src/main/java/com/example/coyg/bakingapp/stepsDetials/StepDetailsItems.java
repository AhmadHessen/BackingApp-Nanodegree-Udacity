package com.example.coyg.bakingapp.stepsDetials;

import android.os.Parcel;
import android.os.Parcelable;

public class StepDetailsItems implements Parcelable
{
    String dec;
    String videoURL;

    public StepDetailsItems(String dec, String videoURL)
    {
        this.dec = dec;
        this.videoURL = videoURL;
    }

    protected StepDetailsItems(Parcel in)
    {
        dec = in.readString ();
        videoURL = in.readString ();
    }

    public static final Creator<StepDetailsItems> CREATOR = new Creator<StepDetailsItems> ()
    {
        @Override
        public StepDetailsItems createFromParcel(Parcel in)
        {
            return new StepDetailsItems (in);
        }

        @Override
        public StepDetailsItems[] newArray(int size)
        {
            return new StepDetailsItems[size];
        }
    };

    public String getDec()
    {
        return dec;
    }

    public String getVideoURL()
    {
        return videoURL;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString (dec);
        parcel.writeString (videoURL);
    }
}
