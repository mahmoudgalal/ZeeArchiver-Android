package com.mg.zeearchiver.dialogs;

import android.app.AlertDialog;
import android.arch.core.util.Function;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mg.zeearchiver.R;
import java.util.concurrent.Callable;

public class ExtractProgressDialog {
    private TextView currItem, percentage;
    private View root;
    final private Context con;
    private AlertDialog pd;
    final private Button cancelBtn;

    public ExtractProgressDialog(Context context, final Callable cancelAction) {
        con = context;
        root = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        currItem = root.findViewById(R.id.current_file);
        currItem.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        currItem.setSelected(true);
        percentage = root.findViewById(R.id.comp_ratio);
        cancelBtn = root.findViewById(R.id.cancel_extraction_btn);
        cancelBtn.setVisibility(View.VISIBLE);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBtn.setEnabled(false);
                try {
                    cancelAction.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public View getRoot() {
        return root;
    }

    public void showDialog(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
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
