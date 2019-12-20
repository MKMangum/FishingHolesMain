package com.mikekmangum.fishingholesmain;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.title);
        TextView snippet_tv = view.findViewById(R.id.snippet);
        ImageView img = view.findViewById(R.id.catch_image_view);
/*
        TextView length_tv = view.findViewById(R.id.length);
        TextView weight_tv = view.findViewById(R.id.weight);
        TextView lure_tv = view.findViewById(R.id.lure);
*/
        name_tv.setText(marker.getTitle());
        snippet_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        Bitmap image = infoWindowData.getImage();
        img.setImageBitmap(image);

/*
        species_tv.setText(infoWindowData.getSpecies());
        length_tv.setText(infoWindowData.getLength());
        weight_tv.setText(infoWindowData.getWeight());
        lure_tv.setText(infoWindowData.getLure());
*/
        return view;
    }
}
