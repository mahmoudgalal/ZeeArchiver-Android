/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */
package com.mg.zeearchiver

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mg.zeearchiver.adapters.FileListAdapter
import com.mg.zeearchiver.utils.FileEntry
import java.io.File
/**
 * A simple FileManger [Fragment].
 */
class FileBrowserFragment : Fragment(), FileListAdapter.OnItemClickListener {
    private lateinit var upBtn: Button
    private lateinit var newFolder: Button
    private lateinit var okBtn: Button
    private lateinit var fileList: RecyclerView
    private var currentPath: String? = null
    private lateinit var listHeader: TextView
    private lateinit var fileListAdapter: FileListAdapter
    private lateinit var fileEntries: MutableList<FileEntry>
    private var browseMode = BROWSE_MODE_FILE
    private var firstTime = false
    private var rootPath: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)
        initializeList()
    }

    private fun initialize(root: View) {
        fileList = root.findViewById(R.id.filelist_recycler)
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.list_separator))
        fileList.addItemDecoration(itemDecoration)
        fileList.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false))
        upBtn = root.findViewById(R.id.path_up)
        newFolder = root.findViewById(R.id.new_folder)
        okBtn = root.findViewById(R.id.okBtn)
        rootPath = Environment.getExternalStorageDirectory().path
        browseMode = arguments?.getInt(FileBrowserActivity.PICK_MODE_KEY) ?: BROWSE_MODE_FILE
        if (browseMode == BROWSE_MODE_FOLDER || browseMode == BROWSE_MODE_SELECT) {
            okBtn.visibility = View.VISIBLE
        } else {
            okBtn.visibility = View.GONE
        }
        listHeader = root.findViewById(R.id.listheader)
        listHeader.text = ""
        fileEntries = mutableListOf()
        fileListAdapter = FileListAdapter(fileEntries, browseMode, this)
        fileList.adapter = fileListAdapter
        upBtn.setOnClickListener(View.OnClickListener {
            val file = File(currentPath)
            if (file.parent != null && !file.path.equals(rootPath, ignoreCase = true)) {
                val task = LoadingTask()
                task.execute(file.parentFile)
            }
        })
        newFolder.setOnClickListener { v: View? -> showNewFolderDialog() }
        okBtn.setOnClickListener(View.OnClickListener {
            if (browseMode == BROWSE_MODE_SELECT) {
                val selectedFiles = mutableListOf<String>()
                for (fe in fileEntries) {
                    if (fe.isSelected) {
                        selectedFiles.add(fe.absolutePath)
                    }
                }
                if (selectedFiles.isNotEmpty()) {
                    val act = activity as FileBrowserActivity
                    act.setSelectedFiles(selectedFiles.toTypedArray())
                }
            } else {
                val currentFile = File(currentPath)
                if (!currentFile.isDirectory) return@OnClickListener
                if (currentFile.canWrite()) {
                    val act = activity as FileBrowserActivity
                    act.setSelectedExtractionPath(currentPath)
                }
            }
        })
    }

    private fun initializeList() {
        val task = LoadingTask()
        task.execute(Environment.getExternalStorageDirectory())
    }

    fun browseTo(location: File): List<FileEntry> {
        currentPath = location.path
        val fl = mutableListOf<FileEntry>()
        if (location.parentFile != null) {
            val fentry = FileEntry()
            fentry.file = location.parentFile
            fl += fentry
        }
        val files = location.listFiles()
        if (files != null)
            for (file in files) {
                val fentry = FileEntry()
                fentry.isDirectory = file.isDirectory
                fentry.file = file
                fl += fentry
            }
        return fl
    }

    override fun onItemLongClicked(view: View, fileEntry: FileEntry): Boolean {
        if (fileEntry.file.isDirectory) {
            if (browseMode == BROWSE_MODE_SELECT) {
                fileEntry.isSelected = !fileEntry.isSelected
                if (fileEntry.isSelected) {
                    view.setBackgroundColor(Color.rgb(0x00, 0x00, 0x99))
                } else view.setBackgroundColor(Color.rgb(0x44, 0x44, 0x44))
            }
        }
        return true
    }

    override fun onItemClicked(view: View, fileEntry: FileEntry) {
        if (fileEntry.file.isDirectory) {
            if (fileEntry.file.canRead()) {
                val task = LoadingTask()
                task.execute(fileEntry.file)
            }
        } else {
            if (browseMode == BROWSE_MODE_FOLDER) {
            } else if (browseMode == BROWSE_MODE_SELECT) {
                fileEntry.isSelected = !fileEntry.isSelected
                if (fileEntry.isSelected) {
                    view.setBackgroundColor(Color.rgb(0x00, 0x00, 0x99))
                } else view.setBackgroundColor(Color.rgb(0x44, 0x44, 0x44))
            } else if (fileEntry.file.canRead()) {
                if (!fileEntry.isDirectory) //filename.endsWith("rar"))
                {
                    val act = context as FileBrowserActivity?
                    act!!.setSelectedFile(fileEntry.file.absolutePath)
                }
            }
        }
    }

    internal inner class LoadingTask : AsyncTask<File, Void?, Void?>() {
        private var pd: ProgressDialog? = null
        override fun onPreExecute() {
            fileEntries.clear()
            fileListAdapter.notifyDataSetChanged()
            if (firstTime) pd = ProgressDialog.show(context, "", "Loading...", true, false)
            firstTime = true
        }

        override fun doInBackground(vararg params: File): Void? {
            val root = params[0]
            val dirContents: List<FileEntry> = browseTo(root)
            fileEntries.add(dirContents[0])
            if (dirContents.size > 1) {
                fileEntries.addAll(dirContents.drop(1).sortedBy {
                    it.fileName.toUpperCase()
                })
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            val mCurrentDir = File(currentPath)
            listHeader.text = if (mCurrentDir.name.isEmpty()) mCurrentDir.path else mCurrentDir.name
            fileListAdapter.notifyDataSetChanged()
            if (mCurrentDir.canWrite()) {
                newFolder.isEnabled = true
                okBtn.isEnabled = true
            } else {
                newFolder.isEnabled = false
                if (browseMode == BROWSE_MODE_SELECT) {
                    okBtn.isEnabled = mCurrentDir.canRead()
                } else okBtn.isEnabled = false
            }
            if (pd != null) pd!!.dismiss()
        }
    }

    private fun showNewFolderDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(resources.getString(R.string.newfolderdialogtitle))
        // Set up the input
        val input = EditText(this.context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setSingleLine(true)
        input.maxLines = 1
        builder.setView(input)
        builder.setPositiveButton(resources.getString(R.string.ok)) { dialog: DialogInterface, _: Int ->
            val newFolderName = input.text.toString()
            if (newFolderName.isNotEmpty()) {
                val mCurrentDir = File(currentPath + File.separator + newFolderName)
                Log.d(TAG, "Creating Directory:" + mCurrentDir.name)
                if (!mCurrentDir.exists()) {
                    if (!mCurrentDir.mkdirs()) Toast.makeText(context, "Couldn't Create Directory", Toast.LENGTH_LONG).show() else {
                        val fentry = FileEntry()
                        fentry.isDirectory = true
                        fentry.file = mCurrentDir
                        fileEntries.add(fentry)
                        fileListAdapter.notifyDataSetChanged()
                    }
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Directory already exists!", Toast.LENGTH_LONG).show()
                }
            }
        }
        builder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    companion object {
        private val TAG = FileBrowserFragment::class.java.simpleName
        const val BROWSE_MODE_FILE = 0
        const val BROWSE_MODE_FOLDER = 1
        const val BROWSE_MODE_SELECT = 2
    }
}