/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mg.zeearchiver.R;
import com.mg.zeearchiver.utils.FileEntry;
import java.util.List;
import static com.mg.zeearchiver.FileBrowserFragment.BROWSE_MODE_SELECT;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private List<FileEntry> items;
    private int browseMode;

    private OnItemClickListener onItemClickListener;

    public FileListAdapter( List<FileEntry> items,int browseMode, OnItemClickListener listener){
        this.browseMode = browseMode;
        this.items = items;
        onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FileEntry fe = items.get(position);
        holder.initialize(fe.getFileName(), fe.isDirectory()?ViewHolder.FILE_TYPE_FOLDER:ViewHolder.FILE_TYPE_FILE);
        if(browseMode == BROWSE_MODE_SELECT)
        {
            if(fe.isSelected())
                holder.itemView.setBackgroundColor(Color.rgb(0x00, 0x00, 0x99));
            else
                holder.itemView.setBackgroundColor(Color.rgb(0x44, 0x44, 0x44));
        }
        holder.itemView.setOnClickListener(view -> {
            if(onItemClickListener != null){
                onItemClickListener.onItemClicked(view,fe);
            }
        });
        holder.itemView.setOnLongClickListener(view -> {
            if(onItemClickListener != null){
                return onItemClickListener.onItemLongClicked(view,fe);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setItems(List<FileEntry> items) {
        this.items = items;
    }

    public interface OnItemClickListener{
        void onItemClicked(View view,FileEntry fileEntry);
        boolean onItemLongClicked(View view,FileEntry fileEntry);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView fileNmae;

        final static int FILE_TYPE_FOLDER = 1;
        final static int FILE_TYPE_FILE = 2;
        public ViewHolder(View itemView) {
            super(itemView);
            fileNmae = itemView.findViewById(R.id.file_name);
        }
        public void initialize(String name,int type)
        {
            fileNmae.setText(name);
            if(type == FILE_TYPE_FOLDER)
                fileNmae.setCompoundDrawablesWithIntrinsicBounds(R.drawable.openfoldericon, 0, 0, 0);
            else
                fileNmae.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }
}
