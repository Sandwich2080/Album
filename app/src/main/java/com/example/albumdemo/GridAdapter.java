package com.example.albumdemo;

import android.content.Context;
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
        ViewHolder holder =null;
        if (convertView == null) {
              convertView = inflater.inflate(R.layout.item_media_file,parent,false);
              holder = new ViewHolder();
              holder.ivImg = convertView.findViewById(R.id.iv_img);
              holder.ivPlay = convertView.findViewById(R.id.iv_play);
              convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        MediaFile file = files.get(position);

        if (file.getType()==MediaFile.TYPE_IMAGE){

            //BitmapFactory.Options opts = new BitmapFactory.Options();
            //opts.inSampleSize = 6;

            //holder.ivImg.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            ImageLoader.getInstance().loadImage(file.getPath(),holder.ivImg);
            holder.ivPlay.setVisibility(View.GONE);
        }else {
            //holder.ivImg.setImageBitmap(ThumbnailUtils.createVideoThumbnail(file.getPath(),MediaStore.Video.Thumbnails.MICRO_KIND));
            ImageLoader.getInstance().loadThumbnail(file.getId(),file.getPath(),holder.ivImg);
            holder.ivPlay.setVisibility(View.VISIBLE);
        }
        //holder.ivImg.setImageBitmap(BitmapFactory.de);

        return convertView;
    }

    public static class ViewHolder {
        public ImageView ivImg;
        public ImageView ivPlay;
    }

}
