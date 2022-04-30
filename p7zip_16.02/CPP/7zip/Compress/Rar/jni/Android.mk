# 
# build 7z Rar Plugin for armeabi and armeabi-v7a CPU
#
#


LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := Rar

LOCAL_CFLAGS := -DANDROID_NDK  -fexceptions \
	-DNDEBUG -D_REENTRANT -DENV_UNIX \
	-DEXTERNAL_CODECS -D_LARGEFILE_SOURCE -D_FILE_OFFSET_BITS=64 -DBREAK_HANDLER \
	-DUNICODE -D_UNICODE -DUNIX_USE_WIN_FILE \
	-I../../../../Windows \
	-I../../../../Common \
	-I../../../../../C \
	-I../../../../myWindows \
	-I../../../../ \
	-I../../../../include_windows

LOCAL_SRC_FILES := \
  ../../../../../CPP/7zip/Common/InBuffer.cpp \
  ../../../../../CPP/7zip/Common/OutBuffer.cpp \
  ../../../../../CPP/7zip/Common/StreamUtils.cpp \
  ../../../../../CPP/7zip/Compress/CodecExports.cpp \
  ../../../../../CPP/7zip/Compress/DllExportsCompress.cpp \
  ../../../../../CPP/7zip/Compress/LzOutWindow.cpp \
  ../../../../../CPP/7zip/Compress/Rar1Decoder.cpp \
  ../../../../../CPP/7zip/Compress/Rar2Decoder.cpp \
  ../../../../../CPP/7zip/Compress/Rar3Decoder.cpp \
  ../../../../../CPP/7zip/Compress/Rar3Vm.cpp \
  ../../../../../CPP/7zip/Compress/Rar5Decoder.cpp \
  ../../../../../CPP/7zip/Compress/RarCodecsRegister.cpp \
  ../../../../../CPP/Common/CRC.cpp \
  ../../../../../CPP/Common/MyVector.cpp \
  ../../../../../CPP/Common/MyWindows.cpp \
  ../../../../../C/7zCrc.c \
  ../../../../../C/7zCrcOpt.c \
  ../../../../../C/Alloc.c \
  ../../../../../C/CpuArch.c \
  ../../../../../C/Ppmd7.c \
  ../../../../../C/Ppmd7Dec.c \

include $(BUILD_SHARED_LIBRARY)

