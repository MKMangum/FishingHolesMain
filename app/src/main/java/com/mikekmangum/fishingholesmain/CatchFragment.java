package com.mikekmangum.fishingholesmain;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;
import java.sql.Timestamp;


public class CatchFragment extends Fragment {


    public static final String ARG_OBJECT = "catch";
    public static final String ARG_TIME = "time";
    public static final String ARG_SPECIES = "species";
    public static final String ARG_LENGTH = "length";
    public static final String ARG_WEIGHT = "weight";
    public static final String ARG_LURE = "lure";
    public static final String ARG_LAT = "lat";
    public static final String ARG_LON = "lon";
    public static final String ARG_TEMP = "temp";
    public static final String ARG_CONDS = "conditions";
    public static final String ARG_BLOB = "picture";
    private TextView  mTextView;
    private ImageView mImageView;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catch, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        mTextView = view.findViewById(R.id.text_display);
        mTextView.setText("Catch " + args.getInt(ARG_OBJECT) +
                         "\nTime: " + args.getString(ARG_TIME) +
                         "\nLength = " + args.getFloat(ARG_LENGTH) +
                         "\nWeight = " + args.getFloat(ARG_WEIGHT));
        mImageView = view.findViewById(R.id.fish_pic);

        if (args.getByteArray(ARG_BLOB) != null) {
            byte[] byteArray = args.getByteArray(ARG_BLOB);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
            mImageView.setImageBitmap(bitmap);
        }
    }
}

