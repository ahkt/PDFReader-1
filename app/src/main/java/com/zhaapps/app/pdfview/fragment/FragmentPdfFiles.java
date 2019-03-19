package com.zhaapps.app.pdfview.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.activity.ViewPdfActivity;
import com.zhaapps.app.pdfview.adapter.PdfFileListAdapter;
import com.zhaapps.app.pdfview.data.DatabaseHandler;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.model.ObjectPdfFile;

import java.io.File;
import java.util.ArrayList;

public class FragmentPdfFiles extends android.support.v4.app.Fragment {
    private ListView listview;
    private LinearLayout lyt_progress;
    private LinearLayout lyt_list;
    private TextView tv_progress;
    private FloatingActionButton bt_scan;
    private ArrayList<File> pdflist = new ArrayList<File>();
    private ArrayList<ObjectPdfFile> pdffiles = new ArrayList<ObjectPdfFile>();
    private GlobalVariable global;
    private DatabaseHandler db;
    int color;
    public FragmentPdfFiles() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pdf_files, container, false);
        global = (GlobalVariable) getActivity().getApplication();
        db = new DatabaseHandler(getActivity().getApplicationContext());

        listview = (ListView) rootView.findViewById(R.id.list_main);
        lyt_progress = (LinearLayout) rootView.findViewById(R.id.lyt_progress);
        lyt_list = (LinearLayout) rootView.findViewById(R.id.lyt_list);
        tv_progress = (TextView) rootView.findViewById(R.id.tv_progress);
        bt_scan = (FloatingActionButton) rootView.findViewById(R.id.fab);

        color = global.getIntPref(global.I_KEY_COLOR, Color.parseColor("#FF594D"));
        bt_scan.setBackgroundTintList(ColorStateList.valueOf(color));

        lyt_progress.setVisibility(View.GONE);
        pdffiles.clear();
        pdffiles = db.getAllPdfFiles();
        listview.setAdapter(new PdfFileListAdapter(getActivity(), pdffiles,color));

        final File envr = Environment.getExternalStorageDirectory();
        new loadFiles(envr).execute("");

        AdView mAdView = (AdView) rootView.findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
                String path = pdffiles.get(arg2).getPath();
                openPdfIntent(path);
            }
        });

        bt_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new loadFiles(envr).execute("");
            }
        });
        return rootView;
    }

    public void getBookmark() {

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

    public ArrayList<ObjectPdfFile> getPdfFromLocal() {
        pdffiles.clear();
        for (int i = 0; i < pdflist.size(); i++) {
            pdffiles.add(new ObjectPdfFile(pdflist.get(i).getName(), pdflist.get(i).getAbsolutePath()));
        }
        return pdffiles;
    }

    public void walkdir(File dir) {
        String pdfPattern = ".pdf";

        File[] listFile = dir.listFiles();

        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)) {
                        pdflist.add(listFile[i]);
                    }
                }
            }
        }
    }

    public class loadFiles extends AsyncTask<String, String, String> {
        private File dir;
        private String pdfPattern = ".pdf";

        public loadFiles(File d) {
            dir = d;
        }

        @Override
        protected void onPreExecute() {
            pdflist.clear();
            bt_scan.setEnabled(false);
            lyt_progress.setVisibility(View.VISIBLE);
            lyt_list.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            walkdir(dir);
            publishProgress("done", "1");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values[1].equals("1")) {
                bt_scan.setEnabled(true);
                lyt_progress.setVisibility(View.GONE);
                lyt_list.setVisibility(View.VISIBLE);
                listview.setAdapter(new PdfFileListAdapter(getActivity(), getPdfFromLocal(),color));
                if (pdffiles.size() > 0) {
                    db.truncateTableScan();
                    db.addPdfFiles(getPdfFromLocal());
                }
            } else {
                tv_progress.setText("");
            }
            super.onProgressUpdate(values);
        }
    }
}
