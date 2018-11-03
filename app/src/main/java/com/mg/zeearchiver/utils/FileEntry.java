/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver.utils;

import java.io.File;

public class FileEntry {

    private String absolutePath = null;
    private String fileName = null;
    private File file = null;
    private boolean isDirectory = false;
    private boolean selected = false;
    public void setFile(File file)
    {
        this.file=file;
        if(file != null)
        {
            absolutePath = file.getAbsolutePath();
            fileName = file.getName();
        }
    }
    public File getFile()
    {
        return file;
    }
    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
