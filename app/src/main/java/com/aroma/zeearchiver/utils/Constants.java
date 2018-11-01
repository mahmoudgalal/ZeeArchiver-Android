/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver.utils;

import com.aroma.zeearchiver.utils.compression.CFormatInfo;
public class Constants {

    public static String[] g_Levels = {
            "Store",
            "Fastest",
            "",
            "Fast",
            "",
            "Normal",
            "",
            "Maximum",
            "",
            "Ultra"
    };
    public enum ELevel
    {
        kStore(0),
        kFastest (1),
        kFast(3),
        kNormal(5),
        kMaximum(7),
        kUltra(9);
        public int value;
        ELevel(int val)
        {
            value=val;
        }
    }
    public enum EEnum
    {
        kAdd,
        kUpdate,
        kFresh,
        kSynchronize
    }
    public enum EMethodID
    {
        kCopy,
        kLZMA,
        kLZMA2,
        kPPMd,
        kBZip2,
        kDeflate,
        kDeflate64,
        kPPMdZip
    }

    public static String kMethodsNames[] =
            {
                    "Copy",
                    "LZMA",
                    "LZMA2",
                    "PPMd",
                    "BZip2",
                    "Deflate",
                    "Deflate64",
                    "PPMd"
            };
    public static EMethodID g_7zMethods[] =
            {
                    EMethodID.kLZMA,
                    EMethodID.kLZMA2,
                    EMethodID.kPPMd,
                    EMethodID.kBZip2
            };
    public static EMethodID g_7zSfxMethods[] =
            {
                    EMethodID.kCopy,
                    EMethodID.kLZMA,
                    EMethodID.kLZMA2,
                    EMethodID.kPPMd
            };
    public static EMethodID g_ZipMethods[] =
            {
                    EMethodID.kDeflate,
                    EMethodID.kDeflate64,
                    EMethodID.kBZip2,
                    EMethodID.kLZMA,
                    EMethodID.kPPMdZip
            };
    public static EMethodID g_GZipMethods[] =
            {
                    EMethodID.kDeflate
            };
    public static EMethodID g_BZip2Methods[] =
            {
                    EMethodID.kBZip2
            };
    public static EMethodID g_XzMethods[] =
            {
                    EMethodID.kLZMA2
            };
    public static CFormatInfo g_Formats[] =
            {
                    new CFormatInfo("",
                            (1 << 0) | (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
                            null, 0,
                            false, false, false, false, false, false),
                    new CFormatInfo("7z",
                            (1 << 0) | (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
                            g_7zMethods, g_7zMethods.length,
                            true, true, true, true, true, true),
                    new CFormatInfo("Zip",
                            (1 << 0) | (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
                            g_ZipMethods, g_ZipMethods.length,
                            false, false, true, false, true, false),
                    new CFormatInfo("GZip",
                            (1 << 1) | (1 << 5) | (1 << 7) | (1 << 9),
                            g_GZipMethods, g_GZipMethods.length,
                            false, false, false, false, false, false),
                    new CFormatInfo("BZip2",
                            (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
                            g_BZip2Methods, g_BZip2Methods.length,
                            false, false, true, false, false, false),
                    new CFormatInfo("xz",
                            (1 << 1) | (1 << 3) | (1 << 5) | (1 << 7) | (1 << 9),
                            g_XzMethods, g_XzMethods.length,
                            false, false, true, false, false, false),
                    new CFormatInfo("Tar",
                            (1 << 0),
                            null, 0,
                            false, false, false, false, false, false)	,
                    new CFormatInfo("wim",
                            (1 << 0),
                            null, 0,
                            false, false, false, false, false, false)
            };

}
