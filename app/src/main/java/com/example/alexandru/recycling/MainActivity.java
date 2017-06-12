package com.example.alexandru.recycling;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HttpAsyncGet.TaskDelegate {
    private Activity mActivity;
    private ArrayList<ItemsElements> mResources = new ArrayList<>();
    private ContactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvContacts);
        new HttpAsyncGet(this, 10, 0).execute("https://dexonline.ro/rss/cuvantul-zilei");
        // final List<Contact> allContacts = Contact.createContactsList(mActivity, 5, 0);

        mAdapter = new ContactsAdapter(mResources);

        rvItems.setAdapter(mAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvItems.setLayoutManager(linearLayoutManager);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // List<Contact> moreContacts = Contact.createContactsList(mActivity, 5, page * 5);
                new HttpAsyncGet(mActivity, 5, page * 5).execute("https://dexonline.ro/rss/cuvantul-zilei");
                // mResources.addAll(moreContacts);

//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mResources.size() - 1);
//                    }
//                });
            }
        };
        rvItems.addOnScrollListener(scrollListener);
    }

    @Override
    public void updateList(ArrayList<ItemsElements> resources) {
        mResources.addAll(resources);
        mAdapter.notifyItemRangeInserted(mAdapter.getItemCount(), mResources.size() - 1);
    }
}


