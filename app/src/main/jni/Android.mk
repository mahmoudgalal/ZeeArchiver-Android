LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := S7z
LOCAL_SRC_FILES := prebuilt/$(TARGET_ARCH_ABI)/lib7z.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := Rar
LOCAL_SRC_FILES := prebuilt/$(TARGET_ARCH_ABI)/libRar.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := zeearchiver
LOCAL_CPPFLAGS := -DEXTERNAL_CODECS -D_REENTRANT -D_7ZIP_ST -DP7Z_ANDROID -D_LARGEFILE_SOURCE -D_LARGE_FILES -DUNICODE -D_UNICODE -Wno-narrowing
   
LOCAL_CFLAGS := -DEXTERNAL_CODECS -D_REENTRANT -D_7ZIP_ST -DP7Z_ANDROID -D_LARGEFILE_SOURCE -D_LARGE_FILES -DUNICODE -D_UNICODE
LOCAL_LDFLAGS += "-Wl,-z,max-page-size=16384"
LOCAL_CPP_FEATURES := exceptions
LOCAL_SRC_FILES := zeearchiver.cpp ExtractGUI.cpp ExtractCallback.cpp\
ConsoleClose.cpp ExtractCallbackConsole.cpp \
BenchCon.cpp List.cpp OpenCallbackConsole.cpp PercentPrinter.cpp \
UpdateCallbackConsole.cpp UserInputUtils.cpp \
ProgramLocation.cpp StringUtils.cpp UpdateCallbackGUI.cpp UpdateGUI.cpp \
Common/CommandLineParser.cpp Common/IntToString.cpp Common/CRC.cpp \
Common/ListFileUtils.cpp Common/StdInStream.cpp Common/StdOutStream.cpp \
Common/MyString.cpp Common/MyWindows.cpp Common/StringConvert.cpp \
Common/StringToInt.cpp Common/UTFConvert.cpp Common/MyVector.cpp \
Common/Wildcard.cpp \
Windows/Error.cpp Windows/DLL.cpp Windows/FileDir.cpp Windows/FileFind.cpp Windows/FileIO.cpp \
Windows/FileName.cpp Windows/PropVariant.cpp Windows/PropVariantConversions.cpp \
Windows/System.cpp Windows/Time.cpp
LOCAL_SRC_FILES += \
CreateCoder.cpp \
FilePathAutoRename.cpp \
FileStreams.cpp \
FilterCoder.cpp \
ProgressUtils.cpp \
StreamUtils.cpp
LOCAL_SRC_FILES += \
ArchiveCommandLine.cpp \
ArchiveExtractCallback.cpp \
ArchiveOpenCallback.cpp \
DefaultName.cpp \
EnumDirItems.cpp \
Extract.cpp \
Bench.cpp \
ExtractingFilePath.cpp \
LoadCodecs.cpp \
OpenArchive.cpp \
PropIDUtils.cpp \
SetProperties.cpp \
SortUtils.cpp \
TempFiles.cpp \
Update.cpp \
UpdateAction.cpp \
UpdateCallback.cpp \
UpdatePair.cpp \
UpdateProduce.cpp
LOCAL_SRC_FILES += \
OutStreamWithCRC.cpp
LOCAL_SRC_FILES += \
C/Alloc.c \
C/Threads.c
LOCAL_SRC_FILES += \
mySplitCommandLine.cpp \
myAddExeFlag.cpp \
wine_date_and_time.cpp
LOCAL_LDLIBS    := -llog
LOCAL_SHARED_LIBRARIES := S7z Rar
include $(BUILD_SHARED_LIBRARY)
