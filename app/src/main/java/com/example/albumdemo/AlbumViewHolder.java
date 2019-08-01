package com.example.albumdemo;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumViewHolder extends RecyclerView.ViewHolder {

    public TextView tvDate;

    public GridView mediaGrid;

    public AlbumViewHolder(@NonNull View itemView) {
        super(itemView);

        mediaGrid = itemView.findViewById(R.id.gv_album);
        tvDate = itemView.findViewById(R.id.tv_date);
    }
}
