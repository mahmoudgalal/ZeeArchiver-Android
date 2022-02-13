package com.mg.zeearchiver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mg.zeearchiver.ExtractCallback;
import com.mg.zeearchiver.R;

public class PassWordDialog extends Dialog {
    Button passCancel, passOk;
    EditText passtext;
    final ExtractCallback callBack;
    private static final String TAG = PassWordDialog.class.getSimpleName();

    public PassWordDialog(final Context context, ExtractCallback callback/*,Unrar unrar*/) {
        super(context);
        setCancelable(false);
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
                    InputMethodManager act = (InputMethodManager) context
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
                    Toast.makeText(context,
                            "Enter a valid password", Toast.LENGTH_LONG).show();
            }
        });

        passCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBack.guiSetPassword(null);
                synchronized (callBack) {
                    callBack.notifyAll();
                }
                dismiss();
            }
        });
        setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(TAG, "Dialog Dismissed....");
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
        Log.d(TAG, "onKeyLongPress() called....");
        return true;
    }
}
