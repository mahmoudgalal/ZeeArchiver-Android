/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver;


import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.mg.zeearchiver.dialogs.ExtractProgressDialog;
import com.mg.zeearchiver.impls.ExtractCallbackImpl;
import com.mg.zeearchiver.utils.Utils;

public class ExtractionActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    static final String TAG = ExtractionActivity.class.getSimpleName();
    public static final int START_FILEBROWSER_REQUEST = 1111;
    public static final int START_DIRECTORYBROWSER_REQUEST = 1112;
    private TextView selectedFileLbl, extractionFolderLbl;
    private String selectedArchivePath, selectedExtractionPath;
    private Uri selectedArchiveUri;

    private boolean lastTaskDone = true;
    private Button extract;
    private Button extractToBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        getWindow().setFeatureInt(Window.PROGRESS_START, 0);
        getWindow().setFeatureInt(Window.PROGRESS_END, 100);
        setContentView(R.layout.activity_main);

        Button openArchive = findViewById(R.id.open_btn);
        openArchive.setOnClickListener(v -> {
            //showFileChooser() ;
            Intent intent = new Intent(ExtractionActivity.this, FileBrowserActivity.class);
            intent.putExtra(FileBrowserActivity.PICK_MODE_KEY, FileBrowserFragment.BROWSE_MODE_FILE);
            startActivityForResult(intent, START_FILEBROWSER_REQUEST);
        });

        selectedFileLbl = findViewById(R.id.select_file);
        //selectedFileLbl.setText("");
        selectedFileLbl.setEllipsize(TruncateAt.MARQUEE);
        selectedFileLbl.setSelected(true);

        extractionFolderLbl = findViewById(R.id.extractionFolder);
        //extractionFolderLbl.setText("");
        extractionFolderLbl.setEllipsize(TruncateAt.MARQUEE);
        extractionFolderLbl.setSelected(true);

        extractToBtn = findViewById(R.id.btn_extract_to);
        extractToBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ExtractionActivity.this, FileBrowserActivity.class);
            intent.putExtra(FileBrowserActivity.PICK_MODE_KEY,
                    FileBrowserFragment.BROWSE_MODE_FOLDER);
            startActivityForResult(intent, START_DIRECTORYBROWSER_REQUEST);
        });
        extract = findViewById(R.id.extract_btn);

        extract.setOnClickListener(v -> {
            if (selectedArchivePath == null || selectedArchivePath.length() == 0) {
                Builder builder = new Builder(ExtractionActivity.this);
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.NO_FILE_CHOSEN_MSG)
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            return;
                        })
                        .setCancelable(false)
                        .show();
                return;
            }
            if (selectedExtractionPath == null || selectedExtractionPath.length() == 0) {

                Builder builder = new Builder(ExtractionActivity.this);
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.NO_EXTR_PATH_SELECTED)
                        .setPositiveButton(R.string.extract_to_hint, (dialog, which) -> {
                            Intent intent = new Intent(ExtractionActivity.this, FileBrowserActivity.class);
                            intent.putExtra(FileBrowserActivity.PICK_MODE_KEY,
                                    FileBrowserFragment.BROWSE_MODE_FOLDER);
                            startActivityForResult(intent, START_DIRECTORYBROWSER_REQUEST);
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .setCancelable(false)
                        .show();

                return;
            }

            if (!lastTaskDone)
                return;

            ArchiveExtractionTask arcExt = new ArchiveExtractionTask();
            if (Utils.INSTANCE.isCachedArchive(this, selectedArchivePath)) {
                arcExt.execute(selectedArchivePath, selectedExtractionPath, selectedArchiveUri.toString());
            } else {
                arcExt.execute(selectedArchivePath, selectedExtractionPath);
            }
        });
        checkStartIntent();

    }

    void checkStartIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            selectedArchiveUri = intent.getData();
            String path = Utils.INSTANCE.getTempPath(this, selectedArchiveUri);
            Log.d(TAG, "File at:" + selectedArchiveUri.getEncodedPath() + " is requested ,path =" + path);
            if (path != null && !path.isEmpty()) {
                selectedFileLbl.setText(path);
                selectedArchivePath = path;
                Log.d(TAG, "Opening Archive done :" + path);
            } else {
                Toast.makeText(this, "Error opening file...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File :"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // In case you used an external file picker instead of the packaged
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = getPath(this, uri);
                    Log.d(TAG, "File Path: " + path);
                    if (path != null) {
                        Archive arc = new Archive();
                        arc.listArchive(path, null);
                    }
                }
                break;
            case START_FILEBROWSER_REQUEST:
                if (resultCode == RESULT_OK) {
                    String path = data.getStringExtra(FileBrowserActivity.SELECTED_FILE_DATA_KEY);
                    if (path != null) {
                        selectedFileLbl.setText(path);
                        selectedArchivePath = path;
                        Log.d(TAG, "Opening Archive done :" + path);
                    }
                }
                break;
            case START_DIRECTORYBROWSER_REQUEST: {
                if (resultCode == RESULT_OK) {
                    String path = data.getStringExtra(FileBrowserActivity.SELECTED_FILE_DATA_KEY);
                    if (path != null) {
                        extractionFolderLbl.setText(path);
                        selectedExtractionPath = path;
                        extract.setText(R.string.extract);
                        Log.d(TAG, "Extracting Archive to: " + path);
                    }
                }
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    void showAlert(String msg, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExtractionActivity.this);
        builder.setMessage(msg).setPositiveButton(getString(R.string.ok), (dialog, which) -> {
        }).create();
        if (title != null)
            builder.setTitle(title);
        builder.show();
    }

    private class ArchiveExtractionTask extends AsyncTask<String, String, Void> {

        private ExtractProgressDialog pd;
        private PowerManager.WakeLock wl = null;
        private boolean errorDetected = false;
        private long curBytes, totalBytes, inSize, outSize;
        private AtomicBoolean shouldCancel = new AtomicBoolean(false);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lastTaskDone = false;
            PowerManager pm = (PowerManager) ExtractionActivity.this.
                    getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG + UUID.randomUUID().toString());
            wl.acquire();
            //pd=ProgressDialog.show(ExtractionActivity.this, "Extracting", "Please wait...", true);
            pd = new ExtractProgressDialog(ExtractionActivity.this , (Callable) () -> {
                cancelExtraction();
                return null;
            });
            pd.showDialog(getString(R.string.extracting));
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                if (values.length == 1) {
                    pd.setCurrentItemText(values[0]);
                } else if (values.length == 2) {
                    if (values[0].equalsIgnoreCase("-E")) {
                        showAlert(values[1], getString(R.string.error));
                    } else if (values[0].equalsIgnoreCase("-P")) {
                        if (totalBytes == 0)
                            totalBytes = 1;
                        int percentValue = (int) (curBytes * 100 / totalBytes);
                        if (percentValue != Integer.MAX_VALUE) {
                            pd.setDialogTitle(getString(R.string.extracting) +
                                    " " + percentValue + "%");
                            setProgress(percentValue * 100);
                        }
                    } else if (values[0].equalsIgnoreCase("-R")) {
                        long packSize = inSize, unpackSize = outSize;
                        if (unpackSize != Long.MAX_VALUE && packSize != Long.MAX_VALUE && unpackSize != 0) {
                            long ratio = packSize * 100 / unpackSize;
                            // pd.setPercentage("Compression Ratio:"+ratio+"%");
                            pd.setPercentage(ratio);
                        }
                    }
                }
            }
        }

        private Void onExtractionUpdate(ExtractCallbackImpl.Update update){
            switch (update.updateType){
                case UPDATE_TYPE_ERROR:
                    publishProgress("-E", update.errorMsg);
                    break;
                case UPDATE_TYPE_CURRENT_FILE:
                    publishProgress(update.currentFilePath);
                    break;
                case UPDATE_TYPE_COMPRESSION_RATIO:
                    inSize = update.inSize;
                    outSize = update.outSize;
                    publishProgress("-R", "");
                    break;
                case UPDATE_TYPE_PROGRESS_PERCENTAGE:
                    curBytes = update.curBytes;
                    totalBytes = update.totalBytes;
                    publishProgress("-P", "");
                    break;
            }
            return null;
        }

        @Override
        protected Void doInBackground(String... params) {
            Archive archive = new Archive();
            if (params != null && params.length > 1) {
                String archivePath = params[0];
                String extractionDirectory = params[1];
                int ret;
                boolean isUri = params.length == 3;
                if (isUri) {
                    String selectedUriString = params[2];
                    Uri selectedArcUri = Uri.parse(selectedUriString);
                    Log.d(TAG, "Copying archive file:" + selectedUriString + " to cache directory");
                    Utils.INSTANCE.copyFileToCache(ExtractionActivity.this, selectedArcUri);
                }
                ExtractCallback extractCallback = new ExtractCallbackImpl(ExtractionActivity.this,
                        this::onExtractionUpdate, shouldCancel);
                if ((ret = archive.extractArchive(archivePath, extractionDirectory, extractCallback)) != 0)    //error case
                {
                    Log.d(TAG, "Extract Archive Error:" + ret);
                    publishProgress("-E", "Extract Archive Error:" + ret);
                    errorDetected = true;
                } else
                    errorDetected = false;

                if (isUri) { // Cached archive should be deleted.
                    Utils.INSTANCE.deleteFile(archivePath);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lastTaskDone = true;
            pd.dismiss();
            setProgress(Window.PROGRESS_END);
            if (!errorDetected) {
                Toast.makeText(ExtractionActivity.this, "Extraction done successfully !", Toast.LENGTH_LONG).show();
            }

            if (wl != null && wl.isHeld()) {
                Log.d(TAG, "Releasing WakeLock...");
                wl.release();
            }
        }

        void cancelExtraction(){
            if(getStatus().equals(Status.RUNNING))
                shouldCancel.set(true);
        }
    }
}
