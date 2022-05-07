package com.mg.zeearchiver.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.mg.zeearchiver.BuildConfig
import java.io.File
import java.io.FileOutputStream

object Utils {

    fun isCachedArchive(context: Context, archivePath: String): Boolean {
        val cacheDirPath: String = context.cacheDir.path
        return archivePath.startsWith(cacheDirPath)
    }

    fun copyFileToCache(context: Context, fileUri: Uri) {
        val returnCursor = context.contentResolver.query(fileUri, null,
                null, null, null) ?: return
        with(returnCursor) {
            use {
                val nameIndex = getColumnIndex(OpenableColumns.DISPLAY_NAME)
                moveToFirst()
                val f = File(context.cacheDir, getString(nameIndex))
                if (f.exists())
                    return
                val outStream = FileOutputStream(f)
                val inStream = context.contentResolver.openInputStream(fileUri)
                outStream.use { outS ->
                    inStream?.use { inS ->
                        inS.copyTo(outS)
                    }
                }
            }
        }
    }

    fun getTempPath(context: Context, uri: Uri?): String? {
        val fileUri = uri ?: return null
        val returnCursor = context.contentResolver.query(fileUri, null,
                null, null, null) ?: return null
        return returnCursor.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            val ret = it.getString(nameIndex)
            val f = File(context.cacheDir, ret)
            f.path
        }
    }

    fun deleteFile(path: String): Boolean {
        val cachedArchive = File(path)
        return cachedArchive.exists() && cachedArchive.delete()
    }

    fun checkAllFilesAccess(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            // Access to all files
            val uri: Uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
            return try {
                context.startActivity(intent)
                false
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace();
                true
            }
        }
        return true
    }

    fun isAllFilesAccessGranted(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        Environment.isExternalStorageManager()
    else
        true
}