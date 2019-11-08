package com.example.albumdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumdemo.util.MediaFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder> {

    private LinkedHashMap<String, ArrayList<MediaFile>> mediaFilesMap;

    private String[] dateArr;

    private Context ctx;

    public void setMediaFilesMap(LinkedHashMap<String, ArrayList<MediaFile>> mediaFilesMap) {
        this.mediaFilesMap = mediaFilesMap;
        if (mediaFilesMap != null) {
            dateArr = mediaFilesMap.keySet().toArray(new String[0]);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View itemView = LayoutInflater.from(ctx).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {

        if (mediaFilesMap == null || mediaFilesMap.isEmpty()) {
            return;
        }

        String date = dateArr[position];
        ArrayList<MediaFile> mediaFiles = mediaFilesMap.get(date);
        holder.tvDate.setText(date);
        GridAdapter gridAdapter = new GridAdapter(ctx);
        holder.mediaGrid.setAdapter(gridAdapter);
        holder.mediaGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gridAdapter.setFiles(mediaFiles);
    }

    @Override
    public int getItemCount() {
        if (mediaFilesMap != null && !mediaFilesMap.isEmpty()) {
            return mediaFilesMap.keySet().size();
        }
        return 0;
    }

    @Override
    public void onViewRecycled(@NonNull AlbumViewHolder holder) {
        super.onViewRecycled(holder);
        //recycleInvisibleImageView(holder);
    }

    /**
     *
     * @param holder
     */
    /*private void recycleInvisibleImageView(@NonNull AlbumViewHolder holder) {
        GridView gridView = holder.mediaGrid;
        //Recycle the images.
        int count = gridView.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = gridView.getChildAt(i);
            ImageView imageView = childView.findViewById(R.id.iv_img);
            if (imageView==null){
                continue;
            }
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }*/
}
