package com.zhaapps.app.pdfview.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.zhaapps.app.pdfview.R;
import com.zhaapps.app.pdfview.data.DatabaseHandler;
import com.zhaapps.app.pdfview.data.GlobalVariable;
import com.zhaapps.app.pdfview.model.Bookmark;

import java.io.File;

public class ViewPdfActivity extends AppCompatActivity {

    private DatabaseHandler db;
    private GlobalVariable global;
    PDFView pdfView;

    Intent intent;
    Uri uri;
    String path = null;

    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpdf);
        global = (GlobalVariable) getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int color = global.getIntPref(global.I_KEY_COLOR, Color.parseColor("#FF594D"));
        ColorDrawable colordrw = new ColorDrawable(color);
        getSupportActionBar().setBackgroundDrawable(colordrw);

        db = new DatabaseHandler(getApplicationContext());
        pdfView = (PDFView) findViewById(R.id.pdfView);


        intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            uri = intent.getData();
            getSupportActionBar().setTitle(getFileName(uri));
        } else {
            Bundle extras = intent.getExtras();
            path = extras.getString("filepath");
            if (path != null) {
                String filename = path.substring(path.lastIndexOf("/") + 1);
                getSupportActionBar().setTitle(filename);
            }
        }

        if (path != null) {
            Log.d("path", path);
            File f = new File(path);
            pdfView.fromFile(f)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .enableAntialiasing(true)
                    .load();

        } else {
            pdfView.fromUri(uri)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .enableAntialiasing(true)
                    .load();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(this, ActivityMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (path != null) {
            File f = new File(path);
            Bookmark book = new Bookmark(f.getName(), path, pdfView.getCurrentPage() + 1);
            //Toast.makeText(getApplicationContext(), "Page : "+getCurPage(), Toast.LENGTH_SHORT).show();
            dialogSaveBookmarkConfirmation(book);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    protected void dialogSaveBookmarkConfirmation(final Bookmark b) {
        final Dialog dialog = new Dialog(ViewPdfActivity.this);
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
        tv_title.setText("Save Bookmark confirmation");
        tv_message.setText("Do you want to bookmark from this '" + b.getName() + "' file?");
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                db.addBookmark(b);
                finish();
                Toast.makeText(getApplicationContext(), b.getPage() + " Page bookmark", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}