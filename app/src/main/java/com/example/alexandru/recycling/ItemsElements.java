package com.example.alexandru.recycling;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandru on 6/12/2017.
 */

public class ItemsElements{
    private String title;
    private String description;
    private String link;
    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    //private ArrayList<ItemsElements> mResources;

    public ItemsElements(String title, String description, String link, Bitmap b) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = b;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
