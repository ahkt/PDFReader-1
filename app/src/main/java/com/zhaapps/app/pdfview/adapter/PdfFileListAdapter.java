package com.zhaapps.app.pdfview.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.model.ObjectPdfFile;

import java.io.File;
import java.util.ArrayList;

public class PdfFileListAdapter extends BaseAdapter {


    private static ArrayList<ObjectPdfFile> itemDetailsrrayList;
    String folder = "";
    Context contex;
    int textcolor;

    private LayoutInflater l_Inflater;

    public PdfFileListAdapter(Context context, ArrayList<ObjectPdfFile> results, int Color) {
        this.contex = context;
        itemDetailsrrayList = results;
        this.textcolor = Color;
        l_Inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public ObjectPdfFile getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.list_item_pdf, null);
            holder = new ViewHolder();
            holder.pdf_name = (TextView) convertView.findViewById(R.id.pdf_name);
            holder.pdf_path = (TextView) convertView.findViewById(R.id.pdf_path);
            holder.icon = (ImageView) convertView.findViewById(R.id.image_icon);
            //holder.lyt			= (LinearLayout) convertView.findViewById(R.id.lyt);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pdf_name.setText(itemDetailsrrayList.get(position).getName());
        holder.pdf_name.setTextColor(textcolor);
        holder.icon.setImageTintList(ColorStateList.valueOf(textcolor));
        holder.pdf_path.setText(new File(itemDetailsrrayList.get(position).getPath()).getParent());

        return convertView;
    }


    static class ViewHolder {
        TextView pdf_name;
        TextView pdf_path;
        ImageView icon;
    }
}
