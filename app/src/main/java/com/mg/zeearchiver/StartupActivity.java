/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver;

import java.util.Random;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils.TruncateAt;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class StartupActivity extends AppCompatActivity {

	private TextView  copyRightLbl ;
	public final String[] EXTERNAL_PERMS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE
	};
    private final int MY_PERMISSIONS_REQUEST = 20101;

    private enum RequestedAction{
    	REQUESTED_ACTION_EXTRACT,
		REQUESTED_ACTION_COMPRESS;
	}
	private  RequestedAction lastAction = RequestedAction.REQUESTED_ACTION_EXTRACT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_startup);
		copyRightLbl = findViewById(R.id.pass_lbl);
		//copyRightLbl.setText("");
		copyRightLbl.setEllipsize(TruncateAt.MARQUEE);			
		copyRightLbl.setSelected(true);
		//copyRightLbl.setText("Copyright � 2014 Mahmoud Galal");
		//Random r=new Random();	
		//copyRightLbl.setBackgroundColor(Color.rgb(r.nextInt(200), 22, r.nextInt(255)));
		
		Button startDecompress = findViewById(R.id.decompress);
		startDecompress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lastAction = RequestedAction.REQUESTED_ACTION_EXTRACT;
				checkStoragePermissionAndRequest();
			}
		});
		
		Button startCompressor= findViewById(R.id.create_archive);
		startCompressor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lastAction = RequestedAction.REQUESTED_ACTION_COMPRESS;
				checkStoragePermissionAndRequest();
			}
		});
		
		Button techInfo = findViewById(R.id.tech_info);
		techInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent=new Intent(StartupActivity.this, InfoActivity.class);
				startActivity(intent);
			}
		});
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Random r=new Random();	
		copyRightLbl.setSelected(true);
		copyRightLbl.setBackgroundColor(Color.rgb(r.nextInt(200), 22, r.nextInt(255)));
	}

	private void checkStoragePermissionAndRequest(){

		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(this,
				EXTERNAL_PERMS[0])
				!= PackageManager.PERMISSION_GRANTED) {

			// Permission is not granted
			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					EXTERNAL_PERMS[0])) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
				Toast.makeText(this,"Please understand that you need to grant " +
						"the app Read/Write Storage permission to be able to use it",
						Toast.LENGTH_LONG).show();
			} else {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(this,
						EXTERNAL_PERMS,
						MY_PERMISSIONS_REQUEST);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}
		} else {
			// Permission has already been granted
			if(lastAction == RequestedAction.REQUESTED_ACTION_COMPRESS) {
				Intent intent = new Intent(StartupActivity.this, CompressActivity.class);
				startActivity(intent);
			}else {

				Intent intent = new Intent(StartupActivity.this, ExtractionActivity.class);
				startActivity(intent);
			}
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						// permission was granted, yay! Do the
						if(lastAction == RequestedAction.REQUESTED_ACTION_COMPRESS) {
							Intent intent = new Intent(StartupActivity.this, CompressActivity.class);
							startActivity(intent);
						}else {

							Intent intent = new Intent(StartupActivity.this, ExtractionActivity.class);
							startActivity(intent);
						}
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request.
		}
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.startup, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case R.id.action_about_item:
				showAboutDialog();
			break;
			case R.id.action_share_item:
				share();
			break;
		}
		return true;//super.onOptionsItemSelected(item);
	}
	
	private void showAboutDialog()
	{
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.about).setCancelable(true).setMessage(R.string.about_msg)
		.setPositiveButton(R.string.ok, null).create();
		builder.show();
	}
	private void share()
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Zee Archiver is awesome !!");
		sendIntent.setType("text/plain");
		startActivity(Intent.createChooser(sendIntent,getString(R.string.action_share) + "..."));
	}

}
