package com.example.coyg.bakingapp.recipeDetails.steps;

public class StepsItem
{
    String dec;
    String shortDescription;
    String videoURL;
    int index;

    public StepsItem(String dec, String shortDescription, String videoURL, int index)
    {
        this.dec = dec;
        this.shortDescription = shortDescription;
        this.videoURL = videoURL;
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public String getShortDescription()
    {
        return shortDescription;
    }

    public String getDec()
    {
        return dec;
    }

    public String getVideoURL()
    {
        return videoURL;
    }
}
