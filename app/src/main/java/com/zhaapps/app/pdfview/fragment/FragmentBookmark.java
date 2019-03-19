package com.zhaapps.app.pdfview.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.activity.ViewPdfActivity;
import com.zhaapps.app.pdfview.adapter.BookmarkListAdapter;
import com.zhaapps.app.pdfview.data.DatabaseHandler;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.model.Bookmark;

import java.io.File;
import java.util.ArrayList;

public class FragmentBookmark extends android.support.v4.app.Fragment {

    private AdView mAdView;

    private GlobalVariable global;
    private ListView list_bookmark;
    public DatabaseHandler db;
    ArrayList<Bookmark> bookmarks = new ArrayList<Bookmark>();
    int color;
    public FragmentBookmark() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bookmark, container, false);
        list_bookmark = (ListView) rootView.findViewById(R.id.list_bookmark);
        global = (GlobalVariable) getActivity().getApplication();
        color = global.getIntPref(global.I_KEY_COLOR, Color.parseColor("#FF594D"));


        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) rootView.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        db = new DatabaseHandler(getActivity().getApplicationContext());
        global = (GlobalVariable) getActivity().getApplication();

        getAllBookmarkFromDb();
        list_bookmark.setAdapter(new BookmarkListAdapter(FragmentBookmark.this, bookmarks,color));

        list_bookmark.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                String path = bookmarks.get(arg2).getPath();
                openPdfIntent(path);
            }
        });

        return rootView;
    }

    public void getAllBookmarkFromDb() {
        bookmarks.clear();
        bookmarks = db.getAllBookmark();
    }

    public void refreshListview() {
        getAllBookmarkFromDb();
        list_bookmark.setAdapter(new BookmarkListAdapter(FragmentBookmark.this, bookmarks,color));
    }

    private void openPdfIntent(String path) {
        if (new File(path).exists()) {
            try {
                final Intent intent = new Intent(getActivity(), ViewPdfActivity.class);
                intent.putExtra("filepath", path);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "File not exist", Toast.LENGTH_SHORT).show();
        }
    }

}
