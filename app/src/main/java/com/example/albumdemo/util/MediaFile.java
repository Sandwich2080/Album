package com.example.albumdemo.util;

public class MediaFile {

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 0;

    private long id;

    private String path;

    private String thumbnailPath;

    private int type;

    private long dateModified;

    public MediaFile() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }
}
