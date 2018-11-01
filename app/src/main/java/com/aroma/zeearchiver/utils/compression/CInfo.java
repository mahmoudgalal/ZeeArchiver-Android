/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver.utils.compression;

import static com.aroma.zeearchiver.utils.Constants.*;

import java.util.Vector;

/**
 * Compression session info
 */
public class CInfo
{
    public EEnum UpdateMode;
    public boolean SolidIsSpecified;
    public boolean MultiThreadIsAllowed;
    public long SolidBlockSize;
    public int NumThreads;

    public Vector<Long> VolumeSizes;

    public int Level;
    public String Method;
    public int Dictionary;
    public boolean OrderMode;
    public int Order;
    public String Options;

    public String EncryptionMethod;

    public boolean SFXMode;
    public boolean OpenShareForWrite;


    public String ArchiveName; // in: Relative for ; out: abs
    public String CurrentDirPrefix;
    public boolean KeepName;

    public boolean GetFullPathName(String result)
    {
        return true;
    }

    public int FormatIndex;

    public String Password;
    public boolean EncryptHeadersIsAllowed;
    public boolean EncryptHeaders;

    void Init()
    {
        Level = Dictionary = Order =0;// UInt32(-1);
        OrderMode = false;
        Method="";
        Options="";
        EncryptionMethod="";
    }
    public CInfo()
    {
        Init();
    }
}
