package com.mg.zeearchiver.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mg.zeearchiver.R;
import java.util.function.Function;

public class PassWordDialog extends Dialog {
    private final Button passCancel, passOk;
    private final EditText passtext;
    private static final String TAG = PassWordDialog.class.getSimpleName();
    private final Function<String,Void> onPassConfirmed;
    private final Function<Void,Void> onPassCanceled;
    private final Function<Void,Void> onDismissed;

    public PassWordDialog(final Context context,
                          Function<String,Void> onPassConfirmed,
                                   Function<Void,Void> onPassCanceled,
                                   Function<Void,Void> onDismissed) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.pass_dialog);
        passCancel = findViewById(R.id.passcancel);
        passOk = findViewById(R.id.passok);
        passtext = findViewById(R.id.passtext);
        this.onPassConfirmed = onPassConfirmed;
        this.onPassCanceled = onPassCanceled;
        this.onDismissed = onDismissed;

        passOk.setOnClickListener(v -> {
            String pass = passtext.getText().toString();
            if (!pass.trim().isEmpty()) {
                InputMethodManager act = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (act != null)
                    act.hideSoftInputFromWindow(passtext.getWindowToken(),0);
                //unrarInst.setPassWord(pass);
                onPassConfirmed.apply(pass);
                dismiss();
            } else
                Toast.makeText(context, "Enter a valid password", Toast.LENGTH_LONG).show();
        });

        passCancel.setOnClickListener(v -> {
           onPassCanceled.apply(null);
            dismiss();
        });
        setOnDismissListener(dialog -> {
            Log.d(TAG, "Dialog Dismissed....");
            onDismissed.apply(null);
        });
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyLongPress() called....");
        return true;
    }
}
