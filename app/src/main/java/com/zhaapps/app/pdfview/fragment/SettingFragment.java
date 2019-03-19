package com.zhaapps.app.pdfview.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.zhaapps.app.pdfview.activity.ActivitySplash;
import com.zhaapps.app.pdfview.GoogleAnalystic;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.data.GlobalVariable;

/**
 * Created by AswinBalaji on 10-Mar-17.
 */
public class SettingFragment extends android.support.v4.app.Fragment {

    private AdView mAdView;

    private LinearLayout lyt_color, lyt_about, lyt_rate, lyt_about1;
    private GlobalVariable global;
    private TextView tv_color;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_setting, container, false);

        global = (GlobalVariable) getActivity().getApplication();
        try {
            Tracker t = ((GoogleAnalystic) getActivity().getApplication()).getDefaultTracker();

            t.setScreenName("Setting Screen");
            t.enableAdvertisingIdCollection(true);

            t.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) rootView.findViewById(R.id.ad_view1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        lyt_color = (LinearLayout) rootView.findViewById(R.id.lyt_color);
        lyt_about = (LinearLayout) rootView.findViewById(R.id.lyt_about);
        lyt_rate = (LinearLayout) rootView.findViewById(R.id.lyt_rate);
        lyt_about1 = (LinearLayout) rootView.findViewById(R.id.lyt_abot);
        tv_color = (TextView) rootView.findViewById(R.id.tv_color);
        tv_color.setText(global.getStringPref(GlobalVariable.S_KEY_COLOR, "default color"));
        actionMenu();
        return rootView;
    }


    private void actionMenu() {
        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        lyt_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogColorChooser();
            }
        });

        lyt_about1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"zha@zhaapps.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                i.putExtra(Intent.EXTRA_TEXT   , "Body content");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lyt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAbout();
            }
        });

        lyt_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

    }

    protected void dialogAbout() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final Button button_ok = (Button) dialog.findViewById(R.id.button_ok);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void dialogColorChooser() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_color_theme);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ListView list = (ListView) dialog.findViewById(R.id.listView_colors);
        final String stringArray[] = getResources().getStringArray(R.array.arr_main_color_name);
        final String colorCode[] = getResources().getStringArray(R.array.arr_main_color_code);
        list.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stringArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setBackgroundColor(Color.parseColor(colorCode[position]));
                textView.setTextColor(Color.WHITE);
                return textView;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                global.setIntPref(GlobalVariable.I_KEY_COLOR, Color.parseColor(colorCode[arg2]));
                global.setStringPref(GlobalVariable.S_KEY_COLOR, stringArray[arg2]);
                tv_color.setText(stringArray[arg2]);
                dialog.dismiss();
                Intent i = new Intent(getActivity(), ActivitySplash.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                getActivity().finish();
            }
        });

        //global.setIntPref(global.I_PREF_COLOR, global.I_KEY_COLOR, getResources().getColor(R.color.red));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
