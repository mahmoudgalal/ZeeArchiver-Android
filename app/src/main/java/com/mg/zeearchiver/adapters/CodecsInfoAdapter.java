/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.zeearchiver.Archive.*;
import com.mg.zeearchiver.R;

import java.util.List;

public class CodecsInfoAdapter extends RecyclerView.Adapter<CodecsInfoAdapter.ViewHolder> {

    private List<Codec> items;

    public CodecsInfoAdapter(List<Codec> items ){
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.codecs_listitem,
                parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Codec codec =  items.get(position);
        holder.setName(codec.codecName);
        holder.setID(""+String.format("%x",codec.codecId));
        holder.setEncoderAssigned(codec.codecEncoderIsAssigned);
        holder.setLibIndex(codec.codecLibIndex+"");
    }

    @Override
    public int getItemCount() {
        return items != null?items.size():0;
    }
    public void setItems(List<Codec> items) {
        this.items = items;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name ,id ,encoderAssigned ,libIndex ;
         ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.codec_name);
            id = itemView.findViewById(R.id.codec_id);
            encoderAssigned = itemView.findViewById(R.id.codec_encoder_assigned);
            libIndex = itemView.findViewById(R.id.codec_libindex);
        }
        public void setName(String name)
        {
            String coname = itemView.getContext().getString(R.string.codec_name);
            this.name.setText(coname+": "+name);

        }
        public void setID(String ID)
        {
            String coID = itemView.getContext().getString(R.string.codec_id);
            id.setText(coID+": "+ID);
        }
        public void setEncoderAssigned(boolean assigned)
        {
            String encAssigned = itemView.getContext().getString(R.string.codec_encoder_assigned);
            String value = assigned?itemView.getContext().getString(R.string.yes):
                    itemView.getContext().getString(R.string.no);
            this.encoderAssigned.setText(encAssigned+": "+value);
        }
        public void setLibIndex(String libindex)
        {
            String coLibIndex = itemView.getContext().getString(R.string.codec_lib_index);
            this.libIndex.setText(coLibIndex+": "+libindex);
        }

    }
}
