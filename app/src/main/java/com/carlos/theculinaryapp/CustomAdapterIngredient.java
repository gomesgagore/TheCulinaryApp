package com.carlos.theculinaryapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class CustomAdapterIngredient extends ArrayAdapter<IngredientItem>{
    public CustomAdapterIngredient(Context context, IngredientItem[] resource) {
        super(context, R.layout.profile_row, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IngredientItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(android.R.id.text1);
        //Log.e("TAG", String.valueOf(name));
        if(item != null)name.setText(item.getName());
        return convertView;
        /*LayoutInflater inf = LayoutInflater.from(getContext());
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
        return customView;*/
    }
}
