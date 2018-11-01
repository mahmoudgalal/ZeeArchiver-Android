/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aroma.zeearchiver.Archive.*;
import com.aroma.zeearchiver.R;

import java.util.List;

public class FormatsInfoAdapter extends RecyclerView.Adapter<FormatsInfoAdapter.ViewHolder> {


    private List<ArchiveFormat> items;

    public FormatsInfoAdapter  (List<ArchiveFormat> items){
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.formats_listitem,
                parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArchiveFormat format = items.get(position);

        holder.setName(format.name);
        holder.setExtensions(format.exts);
        holder.setSignature(format.StartSignature);
        holder.setUpdatable(format.UpdateEnabled);
        holder.setKeepName(format.KeepName);
    }

    @Override
    public int getItemCount() {
        return items != null?items.size():0;
    }
    public void setItems(List<ArchiveFormat> items) {
        this.items = items;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name ,extns ,signature ,updatable ,keepName ;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.format_name);
            extns = itemView.findViewById(R.id.format_ext);
            signature = itemView.findViewById(R.id.format_sig);
            updatable = itemView.findViewById(R.id.format_updatable);
            keepName =  itemView.findViewById(R.id.format_keepname);
        }
        public void setName(String fname)
        {
            name.setText(itemView.getContext().getString(R.string.format_name)+": "+fname);
        }
        public void setExtensions(String exts)
        {
            extns.setText(itemView.getContext().getString(R.string.format_extensions)+": "+exts);
        }
        public void setSignature(String sig)
        {
            String s="";
            for(char c:sig.toCharArray())
            {
                if(c > 0x20 && c < 0x80)
                {
                    s+=c;
                }
                else
                {
                    s+=String.format("%x", (int)c);
                }
            }
            signature.setText(itemView.getContext().getString(R.string.format_signature)+": "+s);
        }
        public void setUpdatable(boolean updatable)
        {
            String formatUpdatable = itemView.getContext().getString(R.string.format_updatable);
            String value = updatable?itemView.getContext().getString(R.string.yes):
                    itemView.getContext().getString(R.string.no);
            this.updatable.setText(formatUpdatable+": "+value);
        }
        public void setKeepName(boolean keepname)
        {
            String formatkeepname = itemView.getContext().getString(R.string.format_keepname);
            String value = keepname?itemView.getContext().getString(R.string.yes):
                    itemView.getContext().getString(R.string.no);
            this.keepName.setText(formatkeepname+": "+value);
        }
    }
}
