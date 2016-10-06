package com.carlos.theculinaryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        imagev.setImageResource(context.getResources().getIdentifier(itemImageFile, "drawable", context.getPackageName()));
        return customView;
    }
}
