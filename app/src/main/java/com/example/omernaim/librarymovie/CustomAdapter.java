package com.example.omernaim.librarymovie;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Omer on 2/21/2017.
 */

public class CustomAdapter extends CursorAdapter {
    public CustomAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_layout, null);
        return v;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String TheTitle= cursor.getString(cursor.getColumnIndex(DBconstant.moviename_db));
        TextView movieTV = (TextView)view.findViewById(R.id.mycustumTV);
        movieTV.setText(TheTitle);
        ImageView imageViewMenu= (ImageView) view.findViewById(R.id.menuPhotoView );

        String theImage = cursor.getString(cursor.getColumnIndex(DBconstant.bitmap64));
        Log.d("fhgf","bfvhf");
        if (theImage==null){
        // imageViewMenu.setImageBitmap(decodeBase64(cursor.getString(cursor.getColumnIndex(DBconstant.bitmap64))));
            imageViewMenu.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            imageViewMenu.setImageBitmap(decodeBase64(theImage));
        }

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
