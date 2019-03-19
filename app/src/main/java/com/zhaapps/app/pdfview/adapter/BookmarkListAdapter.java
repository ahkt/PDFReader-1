package com.zhaapps.app.pdfview.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.fragment.FragmentBookmark;
import com.zhaapps.app.pdfview.model.Bookmark;

import java.util.ArrayList;

public class BookmarkListAdapter extends BaseAdapter {


    private static ArrayList<Bookmark> itemDetailsrrayList;
    String folder = "";
    Context contex;

    int textcolor;

    private LayoutInflater l_Inflater;

    private FragmentBookmark bookmark;

    public BookmarkListAdapter(FragmentBookmark b, ArrayList<Bookmark> results, int color) {
        this.contex = b.getActivity().getApplicationContext();
        textcolor = color;
        itemDetailsrrayList = results;
        bookmark = b;
        l_Inflater = LayoutInflater.from(this.contex);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public Bookmark getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.list_item_bookmark, null);
            holder = new ViewHolder();
            holder.pdf_name = (TextView) convertView.findViewById(R.id.pdf_name);
            holder.pdf_page = (TextView) convertView.findViewById(R.id.pdf_page);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
            holder.img_icon = (ImageView) convertView.findViewById(R.id.image_icon);
            //holder.lyt			= (LinearLayout) convertView.findViewById(R.id.lyt);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pdf_name.setText(itemDetailsrrayList.get(position).getName());
        holder.pdf_name.setTextColor(textcolor);
        holder.pdf_page.setText("Last page : " + itemDetailsrrayList.get(position).getPage());
        holder.img_icon.setImageTintList(ColorStateList.valueOf(textcolor));
        holder.img_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(contex, "Delete success", Toast.LENGTH_LONG).show();
                dialogDeleteConfirmation(itemDetailsrrayList.get(position));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView pdf_name;
        TextView pdf_page;
        ImageView img_delete, img_icon;
    }

    protected void dialogDeleteConfirmation(final Bookmark b) {
        final Dialog dialog = new Dialog(bookmark.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_confirm);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final Button button_no = (Button) dialog.findViewById(R.id.button_no);
        final Button button_yes = (Button) dialog.findViewById(R.id.button_yes);
        final TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        final TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
        tv_title.setText("Delete confirmation");
        tv_message.setText("Are you sure want to remove bookmark from '" + b.getName() + "'?");
        button_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                bookmark.db.deleteBookmark(b);
                bookmark.refreshListview();
                Toast.makeText(contex, "Delete success", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
