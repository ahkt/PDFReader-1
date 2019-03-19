package com.zhaapps.app.pdfview.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.activity.ViewPdfActivity;
import com.zhaapps.app.pdfview.adapter.FileArrayAdapter;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.model.Option;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentBrowse extends android.support.v4.app.Fragment {

    private AdView mAdView;

    private File currentDir;
    private FileArrayAdapter adapter;
    private FileFilter fileFilter;
    private File fileSelected;
    private ArrayList<String> extensions;

    private ListView listview;
    private TextView textView_position;
    private File envr;
    private GlobalVariable global;
int color;
    public FragmentBrowse() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
        global = (GlobalVariable) getActivity().getApplication();
        color = global.getIntPref(global.I_KEY_COLOR, Color.parseColor("#FF594D"));

        listview = (ListView) rootView.findViewById(R.id.listView_brows);
        textView_position = (TextView) rootView.findViewById(R.id.textView_position);

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) rootView.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


        envr = Environment.getExternalStorageDirectory();

        //Toast.makeText(getActivity(), Tools.getRootPath(envr.getAbsolutePath()), Toast.LENGTH_SHORT).show();
        currentDir = envr;
        //new File(Tools.getRootPath(envr.getAbsolutePath()));
        fill(currentDir);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Option o = adapter.getItem(position);
                if (o.isFolder() || o.isParent()) {
                    currentDir = new File(o.getPath());
                    fill(currentDir);
                } else {
                    //onFileClick(o);
                    openPdfIntent(o.getPath());
                    //Toast.makeText(getActivity(), "Open File", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }


    private void fill(File f) {
        File[] dirs = null;
        if (fileFilter != null)
            dirs = f.listFiles(fileFilter);
        else
            dirs = f.listFiles();

        textView_position.setText(getString(R.string.currentDir) + " : " + f.getAbsolutePath());
        List<Option> dir = new ArrayList<Option>();
        List<Option> fls = new ArrayList<Option>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory() && !ff.isHidden())
                    dir.add(new Option(ff.getName(), getString(R.string.folder), ff.getAbsolutePath(), true, false));
                else {
                    if (!ff.isHidden() && isPdf(ff))
                        fls.add(new Option(ff.getName(), getString(R.string.fileSize) + ": " + ff.length(), ff.getAbsolutePath(), false, false));
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase(envr.getName())) {
            if (f.getParentFile() != null)
                dir.add(0, new Option(getString(R.string.parentDirectory), "...", f.getParent(), false, true));
        }
        adapter = new FileArrayAdapter(getActivity(), R.layout.list_item_filechooser, dir, color);
        listview.setAdapter(adapter);
    }

    public boolean isPdf(File f) {
        if (f.getName().endsWith(".pdf")) {
            return true;
        } else {
            return false;
        }
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
