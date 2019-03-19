package com.zhaapps.app.pdfview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.model.Option;

import java.util.List;

public class FileArrayAdapter extends ArrayAdapter<Option> {

    private Context c;
    private int id;
    private List<Option> items;
    GlobalVariable global;
    int textcolor;


    public FileArrayAdapter(Context context, int textViewResourceId, List<Option> objects, int Color) {
        super(context, textViewResourceId, objects);
        this.c = context;
        this.id = textViewResourceId;
        this.items = objects;
        this.textcolor = Color;
    }

    public Option getItem(int i) {
        return items.get(i);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final Option o = items.get(position);
        if (o != null) {
            ImageView im = (ImageView) v.findViewById(R.id.img1);
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);

            if (o.getData().equalsIgnoreCase("folder")) {
                im.setImageResource(R.drawable.folder);
                im.setImageTintList(ColorStateList.valueOf(textcolor));
            } else if (o.getData().equalsIgnoreCase("...")) {
                im.setImageResource(R.drawable.folder);
                im.setImageTintList(ColorStateList.valueOf(textcolor));
            } else {
                String name = o.getName().toLowerCase();
                if (name.endsWith(".pdf")) {
                    im.setImageResource(R.drawable.pdf_icon);
                    im.setImageTintList(ColorStateList.valueOf(textcolor));
                }
            }

            if (t1 != null)
                t1.setText(o.getName());
            if (t1 != null) {
                t1.setTextColor(textcolor);
            }

            if (t2 != null)
                t2.setText(o.getData());

        }
        return v;
    }

}
