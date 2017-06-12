package com.example.alexandru.recycling;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandru on 6/12/2017.
 */

public class Contact {
    private Bitmap mImage;
    private String mName;

    public Contact(String name, Bitmap image) {
        mName = name;
        mImage = image;
    }

    public String getName() {
        return mName;
    }

    public Bitmap getImage() {
        return mImage;
    }

    private static int lastContactId = 0;

    public static List<Contact> createContactsList(Activity activity, int numContacts, int offset) {
        List<Contact> contacts = new ArrayList<Contact>();
        int number_of_items = 0;
        File directory = activity.getCacheDir();
        File[] listOfFiles = directory.listFiles();
        if(listOfFiles.length > 0 ) {
            number_of_items = (listOfFiles.length < numContacts + offset?listOfFiles.length:(numContacts + offset));

            for (int i = offset; i < number_of_items; i++) {
                Bitmap b = BitmapFactory.decodeFile(listOfFiles[i].getAbsolutePath());
                contacts.add(new Contact(listOfFiles[i].getName(), b));
            }
        }
        return contacts;
    }
}
