package com.mg.zeearchiver.impls;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.mg.zeearchiver.ExtractCallback;
import com.mg.zeearchiver.dialogs.PassWordDialog;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import static com.mg.zeearchiver.Archive.Constants.E_ABORT;
import static com.mg.zeearchiver.Archive.Constants.E_FAIL;
import static com.mg.zeearchiver.Archive.Constants.S_OK;

public class ExtractCallbackImpl implements ExtractCallback {
    private final static String TAG = ExtractCallbackImpl.class.getSimpleName();
    private String pass = null;
    private long totalBytes, totalFiles, curFiles;
    private final ReentrantLock passwordLock = new ReentrantLock();
    private final Condition passwordSetCondition = passwordLock.newCondition();
    private AtomicBoolean passSet = new AtomicBoolean(false);
    private AtomicBoolean shouldCancel;
    private Context context;
    private Function<Update, Void> onUpdate;

    public ExtractCallbackImpl(Context context, Function<Update, Void> onUpdate, AtomicBoolean shouldCancel) {
        this.shouldCancel = shouldCancel;
        this.context = context;
        this.onUpdate = onUpdate;
    }

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
        return 0;
    }

    @Override
    public long showMessage(String message) {
        return 0;
    }

    @Override
    public long setTotal(long total) {
        totalBytes = total;
        return 0;
    }

    @Override
    public long setRatioInfo(long inSize, long outSize) {
        //Log.i(TAG,"InSize is:"+inSize+", outSize is:"+outSize);
        Update update = new Update();
        update.updateType = Update.UpdateType.UPDATE_TYPE_COMPRESSION_RATIO;
        update.inSize = inSize;
        update.outSize = outSize;
        onUpdate.apply(update);
        return 0;
    }

    @Override
    public long setPassword(String password) {
        return 0;
    }

    @Override
    public long setOperationResult(int operationResult, long numfiles, boolean encrypted) {
        curFiles = numfiles;
        String error = "Unknown Error!";
        if (operationResult != 0)//Error case
        {

            switch (operationResult) {
                case 1:// kUnSupportedMethod
                    error = "Error:UNSUPPORTED_METHOD ";
                    break;
                case 2: //kDataError
                    if (encrypted)
                        error = "Error:DATA_ERROR_ENCRYPTED";
                    else
                        error = "Error:DATA_ERROR";
                    break;
                case 3: //kCRCError
                    if (encrypted)
                        error = "Error:CRC_ENCRYPTED";
                    else
                        error = "CRC Error";
                    break;
                default:
                    Log.d(TAG, "operation failed with Result:" + error);
            }
            Log.d(TAG, "operation ended with Error:" + error);
            return E_FAIL;
            //publishProgress(error);
            //addErrorMessage(error);
        }
        Log.d(TAG, "operation ended with Result:" + operationResult);
        return S_OK;
    }

    @Override
    public long setNumFiles(long numFiles) {
        totalFiles = numFiles;
        return 0;
    }

    @Override
    public long setCurrentFilePath(String filePath, long numFilesCur) {
        curFiles = numFilesCur;
        Update update = new Update();
        update.updateType = Update.UpdateType.UPDATE_TYPE_CURRENT_FILE;
        update.currentFilePath = filePath;
        onUpdate.apply(update);
        return 0;
    }

    @Override
    public long setCompleted(long value) {
        Update update = new Update();
        update.updateType = Update.UpdateType.UPDATE_TYPE_PROGRESS_PERCENTAGE;
        update.curBytes = value;
        update.totalBytes = totalBytes;
        onUpdate.apply(update);
        return 0;
    }

    @Override
    public long prepareOperation(String name, boolean isFolder,
                                 int askExtractMode, long position) {
        return 0;
    }

    @Override
    public boolean open_WasPasswordAsked() {
        return false;
    }

    @Override
    public long open_SetTotal(long numFiles, long numBytes) {
        totalFiles = numFiles;
        return 0;
    }

    @Override
    public long open_SetCompleted(long numFiles, long numBytes) {
        return 0;
    }

    @Override
    public long open_GetPasswordIfAny(String password) {
        return 0;
    }

    @Override
    public long open_CryptoGetTextPassword(String password) {
        return 0;
    }

    @Override
    public void open_ClearPasswordWasAskedFlag() {
    }

    @Override
    public long open_CheckBreak() {
        // to break(cancel) the process return any value other than 0 (S_OK)
        Log.d(TAG, "open_CheckBreak is called ");
        return shouldCancel.get() ? E_ABORT : S_OK;
    }

    @Override
    public void openResult(String name, long result, boolean encrypted) {
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
        return 0;
    }

    @Override
    public void extractResult(long result) {
    }

    @Override
    public String cryptoGetTextPassword(String password) {
        ShowPasswordDialog();
        passwordLock.lock();
        try {
            while (!passSet.get()) {
                try {
                    passwordSetCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return pass;
        } finally {
            passwordLock.unlock();
        }
    }

    @Override
    public void beforeOpen(String name) {
    }

    @Override
    public long askWrite(String srcPath, int srcIsFolder, long srcTime,
                         long srcSize, String destPathRequest, String destPathResult,
                         int writeAnswer) {
        return 0;
    }

    @Override
    public long askOverwrite(String existName, long existTime, long existSize,
                             String newName, long newTime, long newSize, int answer) {
        return 0;
    }

    @Override
    public void addErrorMessage(String message) {
        Update update = new Update();
        update.updateType = Update.UpdateType.UPDATE_TYPE_ERROR;
        update.errorMsg = message;
        onUpdate.apply(update);
    }

    private void ShowPasswordDialog() {
        Handler handler = new Handler(context.getApplicationContext().getMainLooper());
        handler.post(() -> {
            PassWordDialog pd = new PassWordDialog(context,
                    this::onPassConfirmed, this::onPassCanceled, this::onPassDialogDismissed);
            pd.show();
        });
    }

    private Void onPassConfirmed(String pass){
        passwordLock.lock();
        try {
            guiSetPassword(pass);
            passwordSetCondition.signalAll();
        } finally {
            passwordLock.unlock();
        }
        return null;
    }

    private Void onPassCanceled(Void v){
        passwordLock.lock();
        try {
            guiSetPassword(null);
            passwordSetCondition.signalAll();
        } finally {
            passwordLock.unlock();
        }
        return null;
    }

    private Void onPassDialogDismissed(Void v){
        passwordLock.lock();
        try {
            if (!guiIsPasswordSet()) {
                guiSetPassword(null);
                passwordSetCondition.signalAll();
            }
        } finally {
            passwordLock.unlock();
        }
        return null;
    }

    public static class Update {
        public UpdateType updateType;
        public String errorMsg, currentFilePath;
        public long curBytes, totalBytes, totalFiles, curFiles, inSize, outSize;

        public enum UpdateType {
            UPDATE_TYPE_ERROR,
            UPDATE_TYPE_COMPRESSION_RATIO,
            UPDATE_TYPE_CURRENT_FILE,
            UPDATE_TYPE_PROGRESS_PERCENTAGE
        }
    }
}
