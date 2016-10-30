package com.audreytroutt.androidbeginners.firstapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PaintingDetailActivity extends AppCompatActivity {

    private int paintingId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            paintingId = extras.getInt("painting_id");
        }

        displayPainting();
    }

    private void displayPainting() {
        Resources res = getResources();
        String artist = res.obtainTypedArray(R.array.painting_artists).getString(paintingId);
        String title = res.obtainTypedArray(R.array.painting_titles).getString(paintingId);
        String desc = res.obtainTypedArray(R.array.painting_descriptions).getString(paintingId);
        int year = res.obtainTypedArray(R.array.painting_years).getInt(paintingId, 0);
        this.setTitle(artist);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.WHITE);
        Picasso.with(this).load(res.obtainTypedArray(R.array.paintings).getResourceId(paintingId, 0)).placeholder(gradientDrawable).into((ImageView) findViewById(R.id.pd_image));

        TextView titleYearView = (TextView) findViewById(R.id.pd_title_year);
        titleYearView.setText(title + " (" + year + ")");

        TextView descView = (TextView) findViewById(R.id.pd_description);
        descView.setText(desc);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null && !TextUtils.isEmpty(data.getQueryParameter("painting_id"))) {
            paintingId = Integer.parseInt(data.getQueryParameter("painting_id"));
            displayPainting();
        }
    }
}


