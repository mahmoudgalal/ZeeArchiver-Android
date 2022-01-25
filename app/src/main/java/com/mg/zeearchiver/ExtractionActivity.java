/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver;


import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExtractionActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    static final String TAG = ExtractionActivity.class.getSimpleName();
    public static final int START_FILEBROWSER_REQUEST = 1111;
    public static final int START_DIRECTORYBROWSER_REQUEST = 1112;
    private TextView selectedFileLbl, console, extractionFolderLbl;
    private String selectedArchivePath, selectedExtractionPath;

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
        openArchive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //showFileChooser() ;
                Intent intent = new Intent(ExtractionActivity.this, FileBrowserActivity.class);
                intent.putExtra(FileBrowserActivity.PICK_MODE_KEY, FileBrowserFragment.BROWSE_MODE_FILE);
                startActivityForResult(intent, START_FILEBROWSER_REQUEST);

            }
        });

        selectedFileLbl = findViewById(R.id.select_file);
        //selectedFileLbl.setText("");
        selectedFileLbl.setEllipsize(TruncateAt.MARQUEE);
        selectedFileLbl.setSelected(true);

        console = findViewById(R.id.console);
        console.setText("");

        extractionFolderLbl = findViewById(R.id.extractionFolder);
        //extractionFolderLbl.setText("");
        extractionFolderLbl.setEllipsize(TruncateAt.MARQUEE);
        extractionFolderLbl.setSelected(true);

        extractToBtn = findViewById(R.id.btn_extract_to);
        extractToBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExtractionActivity.this, FileBrowserActivity.class);
                intent.putExtra(FileBrowserActivity.PICK_MODE_KEY,
                        FileBrowserFragment.BROWSE_MODE_FOLDER);
                startActivityForResult(intent, START_DIRECTORYBROWSER_REQUEST);
            }
        });
        extract = findViewById(R.id.extract_btn);

        extract.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectedArchivePath == null || selectedArchivePath.length() == 0) {
                    AlertDialog.Builder builder = new Builder(ExtractionActivity.this);
                    builder.setTitle(R.string.warning)
                            .setMessage(R.string.NO_FILE_CHOSEN_MSG)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setCancelable(false)
                            .show();
                    return;
                }
                if (selectedExtractionPath == null || selectedExtractionPath.length() == 0) {

                    AlertDialog.Builder builder = new Builder(ExtractionActivity.this);
                    builder.setTitle(R.string.warning)
                            .setMessage(R.string.NO_EXTR_PATH_SELECTED)
                            .setPositiveButton(R.string.extract_to_hint, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(ExtractionActivity.this, FileBrowserActivity.class);
                                    intent.putExtra(FileBrowserActivity.PICK_MODE_KEY,
                                            FileBrowserFragment.BROWSE_MODE_FOLDER);
                                    startActivityForResult(intent, START_DIRECTORYBROWSER_REQUEST);
                                }
                            })
                            .setNegativeButton(R.string.cancel, null)
                            .setCancelable(false)
                            .show();

                    return;
                }

                if (!lastTaskDone)
                    return;

                ArchiveExtractCallback arcExt = new ArchiveExtractCallback();
                arcExt.execute(selectedArchivePath, selectedExtractionPath);
            }
        });
        checkStartIntent();

    }

    void checkStartIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            String path = getPath(this, intent.getData());
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
        // TODO Auto-generated method stub
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
        builder.setMessage(msg).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        }).create();
        if (title != null)
            builder.setTitle(title);
        builder.show();
    }

    class ExtractProgressDialogView {
        TextView currItem, percentage;
        View root;
        Context con;
        AlertDialog pd;

        public ExtractProgressDialogView(Context context, int layout) {
            con = context;
            root = LayoutInflater.from(context).inflate(layout, null);
            currItem = (TextView) root.findViewById(R.id.current_file);
            currItem.setEllipsize(TruncateAt.MARQUEE);
            currItem.setSelected(true);
            percentage = (TextView) root.findViewById(R.id.comp_ratio);
        }

        public View getRoot() {
            return root;
        }

        public void showDialog(String title) {
            //view =new UpdateProgressDialogView(mainScreen.getContext(), R.layout.progress_dialog);
            AlertDialog.Builder builder = new Builder(con);
            pd = builder.setView(root).setTitle(title)
                    .setCancelable(false).create();
            pd.getWindow().setWindowAnimations(R.style.moving_dialog);
            pd.show();
        }

        public void dismiss() {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        }

        public void setDialogTitle(String title) {
            pd.setTitle(title);
        }

        public void setDialogTitle(int resid) {
            pd.setTitle(resid);
        }

        public void setCurrentItemText(String st) {
            currItem.setText(st);
        }

        public void setPercentage(String st) {
            percentage.setText(st);
        }

        public void setPercentage(long percent) {
            //"Compression Ratio:"+ratio+"%"
            percentage.setText(con.getString(R.string.compression_ratio) + ":" + percent + "%");
        }
    }

    class ArchiveExtractCallback extends AsyncTask<String, String, Void> {

        private ExtractProgressDialogView pd;
        private PowerManager.WakeLock wl = null;
        private boolean errorDetected = false;
        private long curBytes, totalBytes, totalFiles, curFiles, inSize, outSize;
        private boolean bytesProgressMode = true;


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            lastTaskDone = false;
            PowerManager pm = (PowerManager) ExtractionActivity.this.
                    getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG + UUID.randomUUID().toString());
            wl.acquire();
            //pd=ProgressDialog.show(ExtractionActivity.this, "Extracting", "Please wait...", true);
            pd = new ExtractProgressDialogView(ExtractionActivity.this, R.layout.progress_dialog);
            pd.showDialog(getString(R.string.extracting));
            console.setText("");
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            if (values != null) {
                if (values.length == 1) {
                    console.setText(values[0]);
                    pd.setCurrentItemText(values[0]);
                } else if (values.length == 2) {
                    if (values[0].equalsIgnoreCase("-E")) {
                        console.setText(getString(R.string.error) + values[1]);

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

        public void ShowPasswordDialog(final ExtractCallback callback) {
            ExtractionActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    PassWordDialog pd = new PassWordDialog(ExtractionActivity.this, callback);
                    pd.show();
                }
            });
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            Archive archive = new Archive();
            ExtractCallback exback;
            if (params != null && params.length == 2) {
                int ret = 0;
                if ((ret = archive.extractArchive(params[0], params[1], exback = new ExtractCallback() {
                    String pass = null;
                    AtomicBoolean passSet = new AtomicBoolean(false);

                    final int S_OK = 0x00000000;
                    final int S_FALSE = 0x00000001;
                    final int E_NOTIMPL = 0x80004001;
                    final int E_NOINTERFACE = 0x80004002;
                    final int E_ABORT = 0x80004004;
                    final int E_FAIL = 0x80004005;
                    final int STG_E_INVALIDFUNCTION = 0x80030001;
                    final int E_OUTOFMEMORY = 0x8007000E;
                    final int E_INVALIDARG = 0x80070057;

                    final int ERROR_NO_MORE_FILES = 0x100123;

                    @Override
                    public void guiSetPassword(String pass) {
                        passSet.set(true);
                        this.pass = pass;
                    }

                    @Override
                    public String guiGetPassword() {
                        return pass;
                    }

                    @Override
                    public boolean guiIsPasswordSet() {
                        return pass != null;
                    }

                    @Override
                    public long thereAreNoFiles() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long showMessage(String message) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long setTotal(long total) {
                        // TODO Auto-generated method stub
                        totalBytes = total;
                        curBytes = 0;
                        return 0;
                    }

                    @Override
                    public long setRatioInfo(long inSize, long outSize) {
                        // TODO Auto-generated method stub
                        //Log.i(TAG,"InSize is:"+inSize+", outSize is:"+outSize);
                        ArchiveExtractCallback.this.inSize = inSize;
                        ArchiveExtractCallback.this.outSize = outSize;
                        publishProgress("-R", "");
                        return 0;
                    }

                    @Override
                    public long setPassword(String password) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long setOperationResult(int operationResult, long numfiles, boolean encrypted) {
                        // TODO Auto-generated method stub
                        curFiles = numfiles;
                        String error = "Unknown Error!";
                        if (operationResult != 0)//Error case
                        {

                            switch (operationResult) {
                                case 1:// kUnSupportedMethod
                                    error = "Error:UNSUPPORTED_METHOD ";//Log.i(TAG,"Error:UNSUPPORTED_METHOD " );
                                    break;
                                case 2: //kDataError
                                    if (encrypted)
                                        error = "Error:DATA_ERROR_ENCRYPTED";//	Log.i(TAG,"Error:DATA_ERROR_ENCRYPTED" );
                                    else
                                        error = "Error:DATA_ERROR";//	 Log.i(TAG,"Error:DATA_ERROR");
                                    break;
                                case 3: //kCRCError
                                    if (encrypted)
                                        error = "Error:CRC_ENCRYPTED";// 	Log.i(TAG,"Error:CRC_ENCRYPTED" );
                                    else
                                        error = "CRC Error";//	 Log.i(TAG,"CRC Error");
                                    break;
                                default:
                                    Log.d(TAG, "operation failed with Result:" + error);
                                    return E_FAIL;
                            }
                            //publishProgress(error);
                            //addErrorMessage(error);
                        }
                        Log.d(TAG, "operation failed with Result:" + error);
                        //Log.i(TAG,"setOperationResult called opres="+ operationResult );
                        return 0;
                    }

                    @Override
                    public long setNumFiles(long numFiles) {
                        // TODO Auto-generated method stub
                        totalFiles = numFiles;
                        return 0;
                    }

                    @Override
                    public long setCurrentFilePath(String filePath, long numFilesCur) {
                        // TODO Auto-generated method stub
                        curFiles = numFilesCur;
                        publishProgress(filePath);
                        return 0;
                    }

                    @Override
                    public long setCompleted(long value) {
                        // TODO Auto-generated method stub
                        curBytes = value;
                        publishProgress("-P", "");
                        return 0;
                    }

                    @Override
                    public long prepareOperation(String name, boolean isFolder,
                                                 int askExtractMode, long position) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public boolean open_WasPasswordAsked() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public long open_SetTotal(long numFiles, long numBytes) {
                        // TODO Auto-generated method stub
                        totalFiles = numFiles;
                        return 0;
                    }

                    @Override
                    public long open_SetCompleted(long numFiles, long numBytes) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long open_GetPasswordIfAny(String password) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long open_CryptoGetTextPassword(String password) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void open_ClearPasswordWasAskedFlag() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public long open_CheckBreak() {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void openResult(String name, long result, boolean encrypted) {
                        // TODO Auto-generated method stub
                        //Log.i(TAG, "openResult="+result);
					/*if(result!=S_OK)
					{
						String txt;
						if(result==S_FALSE)
						{
							Log.i(TAG,(encrypted?"Cannot open encrypted archive":"Cannot open archive")
								+" :"+name);
							txt=encrypted?"Cannot open encrypted archive":"Cannot open archive";
						}
						else
						{
							if (result == E_OUTOFMEMORY)
							{//E_OUTOFMEMORY
								Log.i(TAG, "Memory Error openning archive");
								txt= "Memory Error openning archive";
							}
							else
							{
								
								switch((int)result) {
							    case ERROR_NO_MORE_FILES   : txt = "No more files"; break ;
							    case E_NOTIMPL             : txt = "E_NOTIMPL"; break ;
							    case E_NOINTERFACE         : txt = "E_NOINTERFACE"; break ;
							    case E_ABORT               : txt = "E_ABORT"; break ;
							    case E_FAIL                : txt = "E_FAIL"; break ;
							    case STG_E_INVALIDFUNCTION : txt = "STG_E_INVALIDFUNCTION"; break ;
							    case E_OUTOFMEMORY         : txt = "E_OUTOFMEMORY"; break ;
							    case E_INVALIDARG          : txt = "E_INVALIDARG"; break ;
							    default:
							      txt = "UnKnown Error";
							  }
							}
						}
						publishProgress("-E",txt);
						return;
					}*/
                        Log.d(TAG, "Archive opened successfully:" + name);
                    }

                    @Override
                    public long messageError(String message) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void extractResult(long result) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public String cryptoGetTextPassword(String password) {
                        // TODO Auto-generated method stub
                        //publishProgress("-s","Pass");
                        ShowPasswordDialog(this);
                        synchronized (this) {
                            while (!passSet.get())
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                        }
                        return pass;
                    }

                    @Override
                    public void beforeOpen(String name) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public long askWrite(String srcPath, int srcIsFolder, long srcTime,
                                         long srcSize, String destPathRequest, String destPathResult,
                                         int writeAnswer) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long askOverwrite(String existName, long existTime, long existSize,
                                             String newName, long newTime, long newSize, int answer) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public void addErrorMessage(String message) {
                        // TODO Auto-generated method stub
                        publishProgress("-E", message);
                    }
                })) != 0)    //error case
                {
                    Log.i(TAG, "Extract Archive Error:" + ret);
                    publishProgress("-E", "Extract Archive Error:" + ret);
                    errorDetected = true;
                } else
                    errorDetected = false;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lastTaskDone = true;
            pd.dismiss();
            setProgress(Window.PROGRESS_END);
            if (!errorDetected) {
                console.setText("");
                Toast.makeText(ExtractionActivity.this, "Extraction done successfully !", Toast.LENGTH_LONG).show();
            }

            if (wl != null && wl.isHeld()) {
                Log.d(TAG, "Releasing WakeLock...");
                wl.release();
            }
        }

    }


    class PassWordDialog extends Dialog {
        Button passCancel, passOk;
        EditText passtext;
        final ExtractCallback callBack;

        public PassWordDialog(Context context, ExtractCallback callback/*,Unrar unrar*/) {
            super(context);
            setCancelable(false);
            //setTitle(R.string.setpassword);
            setContentView(R.layout.pass_dialog);
            passCancel = findViewById(R.id.passcancel);
            passOk = findViewById(R.id.passok);
            passtext = findViewById(R.id.passtext);
            callBack = callback;

            passOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String pass = passtext.getText().toString();
                    if (!pass.trim().isEmpty()) {
                        InputMethodManager act = (InputMethodManager) ExtractionActivity.this
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (act != null)
                            act.hideSoftInputFromWindow(passtext.getWindowToken()
                                    , 0);
                        //unrarInst.setPassWord(pass);
                        callBack.guiSetPassword(pass);
                        synchronized (callBack) {
                            callBack.notifyAll();
                        }
                        dismiss();
                    } else
                        Toast.makeText(ExtractionActivity.this,
                                "Enter a valid password", Toast.LENGTH_LONG).show();
                }
            });

            passCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //unrarInst.setPassWord(null);

                    callBack.guiSetPassword(null);
                    synchronized (callBack) {
                        callBack.notifyAll();
                    }
                    dismiss();
                }
            });
            //this.
            setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
					Log.i(TAG,"Dialog Dismissed....");
                    if (!callBack.guiIsPasswordSet()) {
                        callBack.guiSetPassword(null);
                        synchronized (callBack) {
                            callBack.notifyAll();
                        }
                    }
                }
            });

        }

        @Override
        public boolean onKeyLongPress(int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            Log.i(TAG,"onKeyLongPress() called....");
            return true;
        }
    }
}
