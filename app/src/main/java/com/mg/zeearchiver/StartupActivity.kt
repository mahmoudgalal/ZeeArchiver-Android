/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */
package com.mg.zeearchiver

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.TruncateAt
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mg.zeearchiver.utils.Utils
import java.util.*

class StartupActivity : AppCompatActivity() {
    private lateinit var copyRightLbl: TextView
    private var lastAction = RequestedAction.REQUESTED_ACTION_EXTRACT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        copyRightLbl = findViewById(R.id.pass_lbl)
        //copyRightLbl.setText("");
        copyRightLbl.ellipsize = TruncateAt.MARQUEE
        copyRightLbl.isSelected = true
        //copyRightLbl.setText("Copyright � 2014 Mahmoud Galal");
        //Random r=new Random();	
        //copyRightLbl.setBackgroundColor(Color.rgb(r.nextInt(200), 22, r.nextInt(255)));
        val startDecompress = findViewById<Button>(R.id.decompress)
        startDecompress.setOnClickListener {
            lastAction = RequestedAction.REQUESTED_ACTION_EXTRACT
            checkStoragePermissionAndRequest()
        }
        val startCompressor = findViewById<Button>(R.id.create_archive)
        startCompressor.setOnClickListener {
            lastAction = RequestedAction.REQUESTED_ACTION_COMPRESS
            checkStoragePermissionAndRequest()
        }
        val techInfo = findViewById<Button>(R.id.tech_info)
        techInfo.setOnClickListener {
            val intent = Intent(this@StartupActivity, InfoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val r = Random()
        copyRightLbl.isSelected = true
        copyRightLbl.setBackgroundColor(Color.rgb(r.nextInt(200), 22, r.nextInt(255)))
    }

    private fun checkStoragePermissionAndRequest() {
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
                Toast.makeText(this, "Please understand that you need to grant " +
                        "the app Read/Write Storage permission to be able to use it",
                        Toast.LENGTH_LONG).show()
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        EXTERNAL_PERMS,
                        MY_PERMISSIONS_REQUEST)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            if (lastAction == RequestedAction.REQUESTED_ACTION_COMPRESS) {
                val intent = Intent(this@StartupActivity, CompressActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@StartupActivity, ExtractionActivity::class.java)
                startActivity(intent)
            }
            Utils.checkAllFilesAccess(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    if (lastAction == RequestedAction.REQUESTED_ACTION_COMPRESS) {
                        val intent = Intent(this@StartupActivity, CompressActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@StartupActivity, ExtractionActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.startup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about_item -> showAboutDialog()
            R.id.action_share_item -> share()
        }
        return true
    }

    private fun showAboutDialog() = with(AlertDialog.Builder(this)) {
        setTitle(R.string.about)
                .setCancelable(true)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.ok, null).show()
    }

    private fun share() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Zee Archiver is awesome !!")
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, getString(R.string.action_share) + "..."))
    }

    companion object {
        private val EXTERNAL_PERMS = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val MY_PERMISSIONS_REQUEST = 20101

        private enum class RequestedAction {
            REQUESTED_ACTION_EXTRACT, REQUESTED_ACTION_COMPRESS
        }
    }
}