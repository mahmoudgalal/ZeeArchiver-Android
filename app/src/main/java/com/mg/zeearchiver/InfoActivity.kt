/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */
package com.mg.zeearchiver

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mg.zeearchiver.adapters.CodecsInfoAdapter
import com.mg.zeearchiver.adapters.FormatsInfoAdapter
import com.mg.zeearchiver.viewmodels.InfoViewModel

class InfoActivity : AppCompatActivity() {
    private lateinit var supportedFormatsList: RecyclerView
    private lateinit var supportedCodecsList: RecyclerView
    private lateinit var codecsInfoAdapter: CodecsInfoAdapter
    private lateinit var formatsInfoAdapter: FormatsInfoAdapter
    private val infoViewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_activity)
        supportedFormatsList = findViewById(R.id.formats_list_recycler)
        supportedCodecsList = findViewById(R.id.codecs_list_recycler)
        supportedFormatsList.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        supportedCodecsList.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.list_separator))
        supportedFormatsList.addItemDecoration(itemDecoration)
        val itemDecoration1 = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration1.setDrawable(resources.getDrawable(R.drawable.list_separator))
        supportedCodecsList.addItemDecoration(itemDecoration1)
        codecsInfoAdapter = CodecsInfoAdapter(null)
        formatsInfoAdapter = FormatsInfoAdapter(null)
        supportedFormatsList.adapter = formatsInfoAdapter
        supportedCodecsList.adapter = codecsInfoAdapter

        with(infoViewModel) {
            load()
            codecs.observe(this@InfoActivity, {
                with(codecsInfoAdapter) {
                    setItems(it)
                    notifyDataSetChanged()
                }
            })
            formats.observe(this@InfoActivity, {
                with(formatsInfoAdapter) {
                    setItems(it)
                    notifyDataSetChanged()
                }
            })
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}