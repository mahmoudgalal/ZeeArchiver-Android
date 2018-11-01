/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver;

import java.util.List;

import com.aroma.zeearchiver.R;
import com.aroma.zeearchiver.adapters.CodecsInfoAdapter;
import com.aroma.zeearchiver.adapters.FormatsInfoAdapter;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;


public class InfoActivity extends AppCompatActivity {

	private RecyclerView supportedFormatsList ,supportedCodecsList;
	private CodecsInfoAdapter codecsInfoAdapter;
	private FormatsInfoAdapter formatsInfoAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_activity);
		supportedFormatsList =  findViewById(R.id.formats_list_recycler);
		supportedCodecsList =   findViewById(R.id.codecs_list_recycler);
		supportedFormatsList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.
				VERTICAL,false));
		supportedCodecsList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.
				VERTICAL,false));
		DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
		itemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_separator));
		supportedFormatsList.addItemDecoration(itemDecoration);
		DividerItemDecoration itemDecoration1 = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
		itemDecoration1.setDrawable(getResources().getDrawable(R.drawable.list_separator));
		supportedCodecsList.addItemDecoration(itemDecoration1);

		codecsInfoAdapter = new CodecsInfoAdapter(null);
		formatsInfoAdapter = new FormatsInfoAdapter(null);

		supportedFormatsList.setAdapter(formatsInfoAdapter);
		supportedCodecsList.setAdapter(codecsInfoAdapter);

		FormatsCodecsLoader task = new FormatsCodecsLoader();
		task.execute();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	class FormatsCodecsLoader extends AsyncTask<Void, Void, Void>
	{
		List<Archive.ArchiveFormat> formats = null;
		List<Archive.Codec> codecs = null;
       @Override
	    protected void onPostExecute(Void result) {
	    	// TODO Auto-generated method stub
	    	super.onPostExecute(result);
	    	formatsInfoAdapter.setItems(formats);
	    	codecsInfoAdapter.setItems(codecs);
		   formatsInfoAdapter.notifyDataSetChanged();
		   codecsInfoAdapter.notifyDataSetChanged();

	    }
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Archive arc = new Archive();
			formats = arc.getSupportedFormats();
			codecs = arc.getSupportedCodecs();
			return null;
		}
	}
}
