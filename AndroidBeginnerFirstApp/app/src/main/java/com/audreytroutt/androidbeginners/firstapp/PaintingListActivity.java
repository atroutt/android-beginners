package com.audreytroutt.androidbeginners.firstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.audreytroutt.androidbeginners.firstapp.paintinglist.PaintingListAdapter;

public class PaintingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_list);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.painting_list_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new PaintingListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mAdapter.getItemViewType(0), 30);
    }
}
