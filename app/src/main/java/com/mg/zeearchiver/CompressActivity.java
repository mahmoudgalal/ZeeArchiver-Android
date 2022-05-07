
/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver;

import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class CompressActivity extends AppCompatActivity {

	final static int START_SELECT_REQUEST = 1011;
	final static int START_FOLDER_BROWSE = 1013;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_compress);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		//getWindow().setFeatureInt(Window.PROGRESS_START, 0);
		//getWindow().setFeatureInt(Window.PROGRESS_END,100);

		setContentView(R.layout.activity_compress);

		FragmentManager fragmentManager = getSupportFragmentManager();
		CompressionFragment compressionFragment =  new CompressionFragment();

		fragmentManager.beginTransaction().replace(R.id.main_container,compressionFragment,
				CompressionFragment.class.getSimpleName()).commit();

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
