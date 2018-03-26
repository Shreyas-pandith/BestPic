package com.example.dell.bestpic;

import android.graphics.Bitmap;

/**
 * Created by DELL on 23-Mar-18.
 */

class ImageItem {
    private Bitmap image;


    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }




}

