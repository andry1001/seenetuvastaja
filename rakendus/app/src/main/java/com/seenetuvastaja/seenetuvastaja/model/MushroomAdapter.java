package com.seenetuvastaja.seenetuvastaja.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seenetuvastaja.seenetuvastaja.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*
Adapteri loomisel on aluseks võetud kood järgnevalt leheküljelt:
https://stackoverflow.com/questions/36512778/custom-adapter-not-showing-any-data-on-listview

 */
public class MushroomAdapter extends ArrayAdapter<Mushroom> {

    private Context mContext;
    private List<Mushroom> mushrooms;
    private float[] probabilities;

    private static DecimalFormat floatFormatter = new DecimalFormat("0.0");


    public MushroomAdapter(Context context, ArrayList<Mushroom> list, float[] probabilities) {
        super(context, 0 , list);
        mContext = context;
        mushrooms = list;
        this.probabilities = probabilities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_listitem,parent,false);

        Mushroom current = mushrooms.get(position);

        try {
            ImageView image = listItem.findViewById(R.id.listitem_image);
            int imgId = this.getContext().getResources().getIdentifier(
                    current.getBinomialName(), "drawable", this.getContext().getPackageName());
            Drawable d = this.getContext().getResources().getDrawable(imgId, null);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            Drawable drawable = new BitmapDrawable(this.getContext().getResources(), Bitmap.createScaledBitmap(bitmap, 150, 150, true));
            image.setImageDrawable(drawable);
        } catch (Exception e) {
            Log.i("Result", "Error laoding picture from resources");
        }

        TextView estonianName = listItem.findViewById(R.id.listitem_estname);
        estonianName.setText(current.getEstonianName());

        TextView binomialName = listItem.findViewById(R.id.listitem_biname);
        binomialName.setText(current.getFormattedBinomialName());

        TextView probability = listItem.findViewById(R.id.listitem_prob);
        float prob = Math.round(100 * probabilities[position]);
        probability.setText("Tõenäosus: " +  floatFormatter.format(prob) + "%");

        TextView poison = listItem.findViewById(R.id.listitem_poison);
        poison.setText(current.getFormattedPoisonClass());

        return listItem;
    }

}
