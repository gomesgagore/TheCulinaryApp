package com.carlos.theculinaryapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

class CustomAdapterProfile extends ArrayAdapter<ProfileItem>{
    public CustomAdapterProfile(Context context, ProfileItem[] resource) {
        super(context, R.layout.profile_row, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View customView = inf.inflate(R.layout.profile_row, parent, false);

        String itemName = getItem(position).getName();
        String itemImageFile = getItem(position).getImageFile();
        TextView name = (TextView) customView.findViewById(R.id.name_text);
        ImageView imagev = (ImageView) customView.findViewById(R.id.image_row);
        imagev.setScaleType(ImageView.ScaleType.CENTER_CROP);

        name.setText(itemName);
        Context context = this.getContext();
        Bitmap b = getPicture(getItem(position).getImageFile());
        if(b==null)
            imagev.setImageResource(context.getResources().getIdentifier(itemImageFile, "drawable", context.getPackageName()));
        else imagev.setImageBitmap(b);
        return customView;
    }

    private Bitmap getPicture(String picUrl){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap bmp = null;
        URL url;
            try {

                url = new URL(picUrl);
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e("Error", e.getMessage());
                }
            } catch (MalformedURLException e) {
                Log.e("Error", e.getMessage());
            }
        return bmp;
    }
}
