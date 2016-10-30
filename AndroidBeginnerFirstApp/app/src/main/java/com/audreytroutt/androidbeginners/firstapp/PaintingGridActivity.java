package com.audreytroutt.androidbeginners.firstapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.audreytroutt.androidbeginners.firstapp.paintinglist.PaintingListAdapter;

public class PaintingGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_grid);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.painting_grid_recycler_view);
        int numberOfColumns = 2;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new PaintingListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mAdapter.getItemViewType(0), 30);
    }
}
