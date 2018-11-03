/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver.utils.compression;


import static com.mg.zeearchiver.utils.Constants.*;

public class CFormatInfo
{
    public String Name;
    public int LevelsMask;
    public EMethodID[]MethodIDs;
    public int NumMethods;
    public boolean Filter;
    public boolean Solid;
    public boolean MultiThread;
    public boolean SFX;
    public boolean Encrypt;
    public boolean EncryptFileNames;

    public  CFormatInfo(String name,int levelsMask,EMethodID[] methodIDs,
                        int numMethods,boolean filter,boolean solid,boolean multiThread,boolean sFX,
                        boolean encrypt,boolean encryptFileNames)
    {
        Name=name;
        LevelsMask=levelsMask;
        MethodIDs=methodIDs;
        NumMethods=numMethods;
        Filter=filter;
        Solid=solid;
        MultiThread=multiThread;
        SFX=sFX;
        Encrypt=encrypt;
        EncryptFileNames=encryptFileNames;
    }
}
