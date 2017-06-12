package com.example.alexandru.recycling;

/**
 * Created by Alexandru on 6/12/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Messenger;
import android.text.Html;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * Created by Alexandru on 6/9/2017.
 */

public class HttpAsyncGet extends AsyncTask<String, Integer, Integer> {
    String server_response;
    Activity mActivity;
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String TAG = "Async";
    private Messenger mMessenger;
    private String[] mResponse = new String[2];
    private TaskDelegate delegate;
    private ArrayList<ItemsElements> mResources = new ArrayList<ItemsElements>();
    private int responseSize = 0;

    private ProgressDialog dialog;
    private int buffer_length = 0;

    public HttpAsyncGet(Activity activity, int buffer_length) {
        mActivity = activity;
        this.buffer_length = buffer_length;
        dialog = new ProgressDialog(activity);
        delegate = (TaskDelegate) activity;

    }

    public interface TaskDelegate {
        public void updateList(ArrayList<ItemsElements> resources);
    }

    private String getFromHtmlTag(String text, String tag) {
        String begin_tag = "<" + tag + ">";
        String end_tag = "</" + tag + ">";
        int begin_tag_index = text.indexOf(begin_tag);
        int end_tag_index = text.indexOf(end_tag, begin_tag_index);
        String sResponse = text.substring(begin_tag_index + begin_tag.length(), end_tag_index);
        //Log.e(TAG, tag + ": " + sResponse);
        return sResponse;
    }

    private void getItems(String response) {

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Processing...");
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        URL url;
        HttpURLConnection urlConnection = null;


        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();
            responseSize = urlConnection.getContentLength();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String title;
                String desc;
                String image;
                String link;
                server_response = readStream(urlConnection.getInputStream());

                //for(int i = 0; i<buffer_length; i++) {
                    while (server_response.substring(server_response.indexOf("<item>")) != "") {
                        server_response = server_response.substring(server_response.indexOf("<item>") + 6);
                        title = getFromHtmlTag(server_response, "title");
                        link = getFromHtmlTag(server_response, "link");
                        desc = getFromHtmlTag(server_response, "description");

                        int startImg = desc.indexOf("<img");
                        int startImgSrc = desc.indexOf("src=\"", startImg);
                        image = desc.substring(startImgSrc + 5, desc.indexOf("\"", startImgSrc + 5));

                        int endImg = desc.indexOf(">", startImg);

                        desc = desc.replace(desc.substring(startImg, endImg + 1), "");
                        desc = desc.replace("<br>", "");
                        desc = Html.fromHtml(desc).toString();

                        URL imageUrl = new URL(image);
                        URLConnection conn = imageUrl.openConnection();
                        Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());

                        File folder = mActivity.getCacheDir();
                        File newFile = new File(folder.getAbsoluteFile(), title + ".jpg");
                        FileOutputStream out = new FileOutputStream(newFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        //Elements elem = new MainActivity.Elements(title, desc, link);
                        mResources.add(new ItemsElements(title, desc, link));
                    //}
                }
            }

        } catch (StringIndexOutOfBoundsException e) {

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseSize;
    }

    @Override
    protected void onPostExecute(Integer result) {
        Log.v(TAG, "Downloaded " + result / 1024 + " KBytes");
        dialog.dismiss();
        delegate.updateList(mResources);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}