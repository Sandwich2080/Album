package com.example.albumdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.albumdemo.util.ImageLoader;
import com.example.albumdemo.util.MediaFile;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<MediaFile> files;

    private Context ctx;

    private LayoutInflater inflater;

    public GridAdapter(Context ctx) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(this.ctx);
    }

    public void setFiles(ArrayList<MediaFile> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (files != null && !files.isEmpty()) {
            return files.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_media_file, parent, false);
            holder = new ViewHolder();
            holder.ivImg = convertView.findViewById(R.id.iv_img);
            holder.ivPlay = convertView.findViewById(R.id.iv_play);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MediaFile file = files.get(position);

        if (file.getType() == MediaFile.TYPE_IMAGE) {

            //BitmapFactory.Options opts = new BitmapFactory.Options();
            //opts.inSampleSize = 6;

            //holder.ivImg.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            ImageLoader.getInstance().loadImage(file.getPath(), holder.ivImg);
            holder.ivPlay.setVisibility(View.GONE);
            holder.ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // preview the image.
                    previewImage(file.getPath());
                }
            });
        } else {
            //holder.ivImg.setImageBitmap(ThumbnailUtils.createVideoThumbnail(file.getPath(),MediaStore.Video.Thumbnails.MICRO_KIND));
            ImageLoader.getInstance().loadThumbnail(file.getId(), file.getPath(), holder.ivImg);
            holder.ivPlay.setVisibility(View.VISIBLE);
            holder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  play the video
                    playVideo(file.getPath());
                }
            });
        }
        //holder.ivImg.setImageBitmap(BitmapFactory.de);


        return convertView;
    }

    private void previewImage(String imagePath){

    }

    private void playVideo(String videoPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(videoPath);
        intent.setDataAndType(uri, "video/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder {
        public ImageView ivImg;
        public ImageView ivPlay;
    }

}
