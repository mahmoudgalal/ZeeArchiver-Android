/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class FileBrowserActivity extends AppCompatActivity {

	public static final String SELECTED_FILE_DATA_KEY = "com.aroma.zeearchiver.SELECTED_FILE";
	public static final String SELECTED_FILES_DATA_KEY = "com.aroma.zeearchiver.SELECTED_FILES";
	public static final String PICK_MODE_KEY = "com.aroma.zeearchiver.PICK_MODE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser);

		FileBrowserFragment fileBrowserFragment = new FileBrowserFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();

		int browseMode = getIntent().getIntExtra(PICK_MODE_KEY,
				FileBrowserFragment.BROWSE_MODE_FILE);
		if(	browseMode == FileBrowserFragment.BROWSE_MODE_FILE){
			setTitle("Select A File:");
		}else if(browseMode == FileBrowserFragment.BROWSE_MODE_FOLDER){
			setTitle("Select A Directory:");
		}
		else
			setTitle("Select File/s:");
		Bundle bundle = new Bundle();
		bundle.putInt(PICK_MODE_KEY,browseMode);

		fileBrowserFragment.setArguments(bundle);

		fragmentManager.beginTransaction().replace(R.id.main_container,fileBrowserFragment,
				FileBrowserFragment.class.getSimpleName()).commit();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.file_browser, menu);
		return true;
	}
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub    		
		setResult(Activity.RESULT_CANCELED);		
    	super.onBackPressed();
    }

	public void setSelectedFile(String absolutePath) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.putExtra(SELECTED_FILE_DATA_KEY, absolutePath);
		setResult(Activity.RESULT_OK,i);
		finish();
	}
	public void setSelectedFiles(String[] files)
	{
		Intent i = new Intent();
		i.putExtra(SELECTED_FILES_DATA_KEY, files);
		setResult(Activity.RESULT_OK,i);
		finish();
	}

	public void setSelectedExtractionPath(String currentPath) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		i.putExtra(SELECTED_FILE_DATA_KEY, currentPath);
		setResult(Activity.RESULT_OK,i);
		finish();
		//setResult(Activity.RESULT_OK);
	}

}
