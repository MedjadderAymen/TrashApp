package com.medjay.trashapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageGride extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> mListPhotoBitmap;
    private Integer[] images={
            R.drawable.ic_insert_photo_black_24dp,
            R.drawable.ic_insert_photo_black_24dp,
            R.drawable.ic_insert_photo_black_24dp,
            R.drawable.ic_insert_photo_black_24dp,
    };

    public ImageGride(Context context,List<Bitmap> ListPhotoBitmap){
        mContext=context;
        mListPhotoBitmap=ListPhotoBitmap;
    }

    @Override
    public int getCount() {
        return mListPhotoBitmap.size();
    }

    public Object getItem(int position){
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView=new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(150,150));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(mListPhotoBitmap.get(i));
        return imageView;
    }

}
