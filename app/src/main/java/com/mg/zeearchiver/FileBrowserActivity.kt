/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */
package com.mg.zeearchiver

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu

class FileBrowserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_browser)
        val fileBrowserFragment = FileBrowserFragment()
        val fragmentManager = supportFragmentManager
        val browseMode = intent.getIntExtra(PICK_MODE_KEY,
                FileBrowserFragment.BROWSE_MODE_FILE)
        setTitle(when (browseMode) {
            FileBrowserFragment.BROWSE_MODE_FILE -> {
                R.string.select_file
            }
            FileBrowserFragment.BROWSE_MODE_FOLDER -> {
                R.string.select_directory
            }
            else -> R.string.select_files
        }
        )
        val bundle = Bundle()
        bundle.putInt(PICK_MODE_KEY, browseMode)
        fileBrowserFragment.arguments = bundle
        fragmentManager.beginTransaction().replace(R.id.main_container, fileBrowserFragment,
                FileBrowserFragment::class.java.simpleName).commit()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.file_browser, menu);
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }

    fun setSelectedFile(absolutePath: String?) {
        val i = Intent()
        i.putExtra(SELECTED_FILE_DATA_KEY, absolutePath)
        setResult(RESULT_OK, i)
        finish()
    }

    fun setSelectedFiles(files: Array<String>?) {
        val i = Intent()
        i.putExtra(SELECTED_FILES_DATA_KEY, files)
        setResult(RESULT_OK, i)
        finish()
    }

    fun setSelectedExtractionPath(currentPath: String?) {
        val i = Intent()
        i.putExtra(SELECTED_FILE_DATA_KEY, currentPath)
        setResult(RESULT_OK, i)
        finish()
        //setResult(Activity.RESULT_OK);
    }

    companion object {
        const val SELECTED_FILE_DATA_KEY = "com.aroma.zeearchiver.SELECTED_FILE"
        const val SELECTED_FILES_DATA_KEY = "com.aroma.zeearchiver.SELECTED_FILES"
        const val PICK_MODE_KEY = "com.aroma.zeearchiver.PICK_MODE"
    }
}