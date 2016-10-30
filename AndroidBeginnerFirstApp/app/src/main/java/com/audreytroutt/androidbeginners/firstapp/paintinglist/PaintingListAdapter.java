package com.audreytroutt.androidbeginners.firstapp.paintinglist;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.audreytroutt.androidbeginners.firstapp.PaintingDetailActivity;
import com.audreytroutt.androidbeginners.firstapp.R;
import com.squareup.picasso.Picasso;

public class PaintingListAdapter extends RecyclerView.Adapter<PaintingListAdapter.ViewHolder> {

    private final Activity context;

    public PaintingListAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_painting, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Resources res = context.getResources();

        // Load images without crashing the app :)
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(Color.WHITE);
        Picasso.with(context).load(res.obtainTypedArray(R.array.paintings).getResourceId(position, 0)).placeholder(gradientDrawable).into(vh.mPaintingImage);

        // Copy the text into the text views
        vh.mPaintingArtist.setText(res.obtainTypedArray(R.array.painting_artists).getString(position));
        vh.mPaintingTitle.setText(res.obtainTypedArray(R.array.painting_titles).getString(position) + " (" + res.obtainTypedArray(R.array.painting_years).getString(position) + ")");
    }

    @Override
    public int getItemCount() {
        return 25;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mPaintingImage;
        public final TextView mPaintingArtist;
        public final TextView mPaintingTitle;

        public ViewHolder(View mListItem) {
            super(mListItem);
            mPaintingImage = (ImageView) mListItem.findViewById(R.id.painting_thumb);
            mPaintingArtist = (TextView) mListItem.findViewById(R.id.painting_artist);
            mPaintingTitle = (TextView) mListItem.findViewById(R.id.painting_title);
            mListItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("XXX", "onClick " + getAdapterPosition());
            Intent i = new Intent(view.getContext(), PaintingDetailActivity.class);
            i.putExtra("painting_id",getAdapterPosition());
            view.getContext().startActivity(i);
        }
    }
}
