package com.zhaapps.app.pdfview.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.zhaapps.app.pdfview.GoogleAnalystic;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.data.DatabaseHandler;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.fragment.AboutFragment;
import com.zhaapps.app.pdfview.fragment.Faceboook;
import com.zhaapps.app.pdfview.fragment.FragmentBookmark;
import com.zhaapps.app.pdfview.fragment.FragmentBrowse;
import com.zhaapps.app.pdfview.fragment.FragmentPdfFiles;
import com.zhaapps.app.pdfview.fragment.PrivacyPolicyFragment;
import com.zhaapps.app.pdfview.fragment.SettingFragment;

@SuppressWarnings("ALL")
public class ActivityMain extends AppCompatActivity {
    public DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    private String sbookmark_counter = "0";
    // used to store app title
    private CharSequence mTitle;


    private DatabaseHandler db;
    private GlobalVariable global;
    private NavigationView navigationView;
    //for ads
    private InterstitialAd mInterstitialAd;

    Toolbar toolbar;
    public ActionBar actionBar;
    ColorDrawable colordrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Tracker t = ((GoogleAnalystic) getApplication()).getDefaultTracker();

            t.setScreenName("Main Screen");
            t.enableAdvertisingIdCollection(true);

            t.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        global = (GlobalVariable) getApplication();

        int color = global.getIntPref(global.I_KEY_COLOR, Color.parseColor("#FF594D"));
        colordrw = new ColorDrawable(color);

        mTitle = mDrawerTitle = getTitle();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.app_name);
        }

        toolbar.setBackgroundColor(color);
        initDrawerMenu();
        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        //prepare ads
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);


        db = new DatabaseHandler(this);
        int ibookmark_counter = db.getAllBookmark().size();


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            android.support.v4.app.Fragment fragment = null;
            fragment = new FragmentPdfFiles();
            if (fragment != null) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
            }
        }
    }

    private void initDrawerMenu() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setBackground(colordrw);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                displayView(menuItem.getItemId(), menuItem.getTitle().toString());
                drawer.closeDrawers();
                return true;
            }
        });

    }

    private void displayView(int id, String title) {
        // update the main content by replacing fragments
        android.support.v4.app.Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (id) {
            case R.id.nav_category:
                fragment = new FragmentPdfFiles();
                showInterstitial();
                break;
            case R.id.nav_bookmark:
                fragment = new FragmentBookmark();
                showInterstitial();
                break;
            case R.id.nav_privacy:
                fragment = new PrivacyPolicyFragment();
                break;
            case R.id.nav_facebook:
                fragment = new Faceboook();
                break;
            case R.id.nav_email:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email)});
                i.putExtra(Intent.EXTRA_SUBJECT, "Feedback of our apps " + getPackageName());
                i.putExtra(Intent.EXTRA_TEXT, "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                showInterstitial();
                break;
            case R.id.nav_browser:
                fragment = new FragmentBrowse();
                break;
            case R.id.nav_app:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.store_name))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.Store_name_1))));
                }
                break;
            case R.id.nav_rate:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hi This is Best App I ever seen ,Try it Friends \n" + " http://play.google.com/store/apps/details?id=" + getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case R.id.nav_setting:
                fragment = new SettingFragment();
                showInterstitial();
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Are you sure want to exit the application")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * show ads
     */
    public void showInterstitial() {
        // Show the ad if it's ready
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

}
