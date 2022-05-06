package com.mg.zeearchiver.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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
}