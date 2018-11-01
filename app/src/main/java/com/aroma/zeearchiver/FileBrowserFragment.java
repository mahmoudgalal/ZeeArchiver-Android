/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver;


import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.aroma.zeearchiver.adapters.FileListAdapter;
import com.aroma.zeearchiver.utils.FileEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileBrowserFragment extends Fragment  implements FileListAdapter.OnItemClickListener {


    private static final String TAG = FileBrowserFragment.class.getSimpleName();
    private Button upBtn = null,newFolder = null,okBtn = null;
    private RecyclerView fileList ;
    private String currentPath = null;
    private TextView listHeader = null;
    private FileListAdapter fileListAdapter = null;
    private ArrayList<FileEntry> fileEntries = null;
    public static int BROWSE_MODE_FILE = 0, BROWSE_MODE_FOLDER = 1 ,BROWSE_MODE_SELECT = 2;
    private int browseMode = BROWSE_MODE_FILE;
    private boolean firstTime = false;
    private String rootPath;


    public FileBrowserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_file_browser, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        initializeList();
    }

    private void initialize(View root)
    {
        fileList = root.findViewById(R.id.filelist_recycler);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_separator
        ));
        fileList.addItemDecoration(itemDecoration);
        fileList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        upBtn = root.findViewById(R.id.path_up);
        newFolder = root.findViewById(R.id.new_folder);
        okBtn = root.findViewById(R.id.okBtn);

        rootPath = Environment.getExternalStorageDirectory().getPath();
        Bundle args = getArguments();
        browseMode  = args.getInt(FileBrowserActivity.PICK_MODE_KEY);

        if(browseMode == BROWSE_MODE_FOLDER || browseMode== BROWSE_MODE_SELECT )
        {
            okBtn.setVisibility(VISIBLE);
        }
        else
        {
            okBtn.setVisibility(GONE);
        }

        listHeader = root.findViewById(R.id.listheader);
        listHeader.setText("");
        fileEntries = new ArrayList<>();
        fileListAdapter  = new FileListAdapter(fileEntries,browseMode, this);
        fileList.setAdapter(fileListAdapter);


        upBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                File file=new File(currentPath);
                if(file.getParent()!=null && !file.getPath().equalsIgnoreCase(rootPath) )
                {
                    LoadingTask task = new LoadingTask();
                    task.execute(file.getParentFile());
                }
            }
        });
        newFolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showNewFolderDialog();
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(browseMode == BROWSE_MODE_SELECT)
                {
                    ArrayList<String> selectedFiles=new ArrayList<String>();
                    for(FileEntry fe:fileEntries)
                    {
                        if(fe.isSelected())
                        {
                            selectedFiles.add(fe.getAbsolutePath());
                        }
                    }
                    if(!selectedFiles.isEmpty())
                    {
                        FileBrowserActivity act=((FileBrowserActivity)getActivity());
                        act.setSelectedFiles(selectedFiles.toArray(new String[]{}));
                    }
                }
                else
                {
                    File currentFile=new File(currentPath);
                    if(!currentFile.isDirectory())
                        return;
                    if(currentFile.canWrite())
                    {
                        FileBrowserActivity act=((FileBrowserActivity)getActivity());
                        act.setSelectedExtractionPath(currentPath);
                    }
                }

            }
        });
    }

    public void initializeList()
    {
        LoadingTask task = new LoadingTask();
        task.execute( Environment.getExternalStorageDirectory());

    }

    public ArrayList<FileEntry> browseTo(File location)
    {
        currentPath = location.getPath();
        File mCurrentDir = location;//new File(location);
        ArrayList<FileEntry> fl=new ArrayList<FileEntry>();

        if (mCurrentDir.getParentFile() != null)
        {
            FileEntry fentry=new FileEntry();
            fentry.setFile(mCurrentDir.getParentFile());
            fl.add(fentry);
        }
        File files[]= mCurrentDir.listFiles();
        if(files != null)
            for (File file :files)
            {
                if (file.isDirectory()) {
                    FileEntry fentry=new FileEntry();
                    fentry.setDirectory(true);
                    fentry.setFile(file);
                    fl.add(fentry);
                }
                else
                {
                    FileEntry fentry = new FileEntry();
                    fentry.setDirectory(false);
                    fentry.setFile(file);
                    fl.add(fentry);
                }
            }
        return fl;
    }

    @Override
    public boolean onItemLongClicked(View view, FileEntry fileEntry) {

        if (fileEntry.getFile().isDirectory())
        {
            if(browseMode== BROWSE_MODE_SELECT )
            {
                fileEntry.setSelected(!fileEntry.isSelected());
                if(fileEntry.isSelected())
                {
                    view.setBackgroundColor(Color.rgb(0x00, 0x00, 0x99));
                }
                else
                    view.setBackgroundColor(Color.rgb(0x44, 0x44, 0x44));
            }
        }
        return true;
    }
    @Override
    public void onItemClicked(View view, FileEntry fileEntry) {
        if (fileEntry.getFile().isDirectory()) {
            LoadingTask task = new LoadingTask();
            task.execute(fileEntry.getFile());
        }
        else
        {
            if(browseMode == BROWSE_MODE_FOLDER )
            {

            }
            else if(browseMode== BROWSE_MODE_SELECT )
            {
                fileEntry.setSelected(!fileEntry.isSelected());
                if(fileEntry.isSelected())
                {
                    view.setBackgroundColor(Color.rgb(0x00, 0x00, 0x99));

                }
                else
                    view.setBackgroundColor(Color.rgb(0x44, 0x44, 0x44));
            }
            else
            if(fileEntry.getFile().canRead())
            {
                if(!fileEntry.isDirectory())//filename.endsWith("rar"))
                {
                    FileBrowserActivity act=((FileBrowserActivity)getContext());
                    act.setSelectedFile(fileEntry.getFile().getAbsolutePath());
                }
            }
        }
    }

    class LoadingTask extends AsyncTask<File , Void, Void>
    {
        private ProgressDialog pd=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            fileEntries.clear();
            //fileListAdapter.clear();
            fileListAdapter.notifyDataSetChanged();
            if(firstTime)
                pd=ProgressDialog.show(getContext(), "", "Loading...", true, false);
            firstTime=true;
        }
        @Override
        protected Void doInBackground(File... params) {
            fileEntries.addAll( browseTo(params[0]));
            Collections.sort(fileEntries, new Comparator<FileEntry>() {
                @Override
                public int compare(FileEntry fileEntry, FileEntry t1) {
                    return fileEntry.getFileName().compareTo(t1.getFileName());
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            File mCurrentDir=new File(currentPath);
            if(listHeader != null)
                listHeader.setText(mCurrentDir.getName().compareTo("") == 0 ? mCurrentDir
                        .getPath() : mCurrentDir.getName());
            if(fileList != null)
            {
                //fileListAdapter = new FileListAdapter(getContext(), fileEntries);
                //fileList.setAdapter(fileListAdapter);
                fileListAdapter.notifyDataSetChanged();
            }
            if(mCurrentDir.canWrite())
            {
                newFolder.setEnabled(true);
                okBtn.setEnabled(true);
            }
            else
            {
                newFolder.setEnabled(false);
                if(browseMode==BROWSE_MODE_SELECT)
                {
                    if(mCurrentDir.canRead())
                        okBtn.setEnabled(true);
                    else
                        okBtn.setEnabled(false);
                }
                else
                    okBtn.setEnabled(false);
            }
            if(pd != null)
                pd.dismiss();
        }
    }

    void showNewFolderDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("New Folder");
        // Set up the input
        final EditText input = new EditText(this.getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT );
        input.setSingleLine(true);
        input.setMaxLines(1);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newFolderName = input.getText().toString();
                if(newFolderName.length()>0)
                {
                    File mCurrentDir=new File(currentPath+File.separator+newFolderName);
                    Log.d(TAG,"Creating Directory:"+ mCurrentDir.getName());
                    if(!mCurrentDir.exists())
                    {
                        if(!mCurrentDir.mkdirs())
                            Toast.makeText(getContext(), "Couldn't Create Directory"
                                    , Toast.LENGTH_LONG).show();
                        else
                        {
                            FileEntry fentry = new FileEntry();
                            fentry.setDirectory(true);
                            fentry.setFile(mCurrentDir);
                            fileEntries.add(fentry);
                            fileListAdapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Directory already exists!"
                                , Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
