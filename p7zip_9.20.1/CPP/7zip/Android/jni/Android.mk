#../../../Windows/Synchronization.cpp      \
#../../../../C/Threads.c                           \

LOCAL_PATH := $(call my-dir)
MY_LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := Threads
LOCAL_C_INCLUDES := ../../../myWindows \
                       ../../../          \
                       ../../../include_windows
LOCAL_SRC_FILES := ../../../../C/Threads.c

include $(BUILD_STATIC_LIBRARY)

LOCAL_PATH := $(MY_LOCAL_PATH)
include $(CLEAR_VARS)
   LOCAL_MODULE    := 7z
   LOCAL_CPPFLAGS := -DEXTERNAL_CODECS -D_REENTRANT -D_7ZIP_ST -DP7Z_ANDROID -D_LARGEFILE_SOURCE -D_FILE_OFFSET_BITS=64 -D_LARGE_FILES -DUNICODE -D_UNICODE
   
   LOCAL_CFLAGS := -DEXTERNAL_CODECS -D_REENTRANT -D_7ZIP_ST -DP7Z_ANDROID -D_LARGEFILE_SOURCE -D_FILE_OFFSET_BITS=64 -D_LARGE_FILES -DUNICODE -D_UNICODE
   LOCAL_CPP_EXTENSION := .c .cpp
   LOCAL_CPP_FEATURES := exceptions
LOCAL_C_INCLUDES := ../../../myWindows \
                       ../../../          \
                       ../../../include_windows
 LOCAL_SRC_FILES :=\
../../../myWindows/myGetTickCount.cpp  \
../../../myWindows/wine_date_and_time.cpp
LOCAL_SRC_FILES+=../../../../C/7zBuf2.c             \
../../../../C/7zStream.c                          \
../../../../C/Aes.c                              \
../../../../C/Alloc.c                         \
../../../../C/Bra.c                              \
../../../../C/Bra86.c                         \
../../../../C/BraIA64.c                      \
../../../../C/BwtSort.c                          \
../../../../C/Delta.c                          \
../../../../C/HuffEnc.c                        \
../../../../C/LzFind.c                         \
../../../../C/LzFindMt.c                       \
../../../../C/Lzma2Dec.c                        \
../../../../C/Lzma2Enc.c                      \
../../../../C/LzmaDec.c                       \
../../../../C/LzmaEnc.c                     \
../../../../C/MtCoder.c                          \
../../../../C/Ppmd7.c                           \
../../../../C/Ppmd7Dec.c                        \
../../../../C/Ppmd7Enc.c                           \
../../../../C/Ppmd8.c                           \
../../../../C/Ppmd8Dec.c                        \
../../../../C/Ppmd8Enc.c                         \
../../../../C/Sha256.c                          \
../../../../C/Sort.c                             \
../../../../C/Threads.c                          \
../../../../C/Xz.c                                \
../../../../C/XzCrc64.c                             \
../../../../C/XzDec.c                          \
../../../../C/XzEnc.c                          \
../../../../C/XzIn.c
 LOCAL_SRC_FILES+=../../../Common/CRC.cpp  \
../../../Common/IntToString.cpp  \
../../../Common/MyMap.cpp  \
../../../Common/MyString.cpp  \
../../../Common/MyWindows.cpp  \
../../../Common/MyXml.cpp  \
../../../Common/StringConvert.cpp    \
../../../Common/StringToInt.cpp       \
../../../Common/UTFConvert.cpp     \
../../../Common/MyVector.cpp     \
../../../Common/Wildcard.cpp

LOCAL_SRC_FILES+=../../../Windows/FileDir.cpp    \
../../../Windows/FileFind.cpp   \
../../../Windows/FileIO.cpp     \
../../../Windows/PropVariant.cpp   \
../../../Windows/PropVariantUtils.cpp     \
../../../Windows/Synchronization.cpp      \
../../../Windows/System.cpp               \
../../../Windows/Time.cpp
LOCAL_SRC_FILES+=../../Common/InBuffer.cpp            \
../../Common/InOutTempBuffer.cpp     \
../../Common/CreateCoder.cpp         \
../../Common/CWrappers.cpp           \
../../Common/FilterCoder.cpp         \
../../Common/LimitedStreams.cpp      \
../../Common/LockedStream.cpp        \
../../Common/MethodId.cpp             \
../../Common/MethodProps.cpp          \
../../Common/MemBlocks.cpp           \
../../Common/OffsetStream.cpp        \
../../Common/OutBuffer.cpp          \
../../Common/OutMemStream.cpp        \
../../Common/ProgressMt.cpp         \
../../Common/ProgressUtils.cpp      \
../../Common/StreamBinder.cpp       \
../../Common/StreamObjects.cpp      \
../../Common/StreamUtils.cpp        \
../../Common/VirtThread.cpp
LOCAL_SRC_FILES+=../../Archive/ArchiveExports.cpp      \
../../Archive/DllExports2.cpp         \
../../Archive/ApmHandler.cpp          \
../../Archive/ArjHandler.cpp          \
../../Archive/Bz2Handler.cpp          \
../../Archive/CpioHandler.cpp         \
../../Archive/CramfsHandler.cpp      \
../../Archive/DebHandler.cpp          \
../../Archive/DeflateProps.cpp        \
../../Archive/DmgHandler.cpp          \
../../Archive/ElfHandler.cpp          \
../../Archive/FatHandler.cpp          \
../../Archive/FlvHandler.cpp          \
../../Archive/GzHandler.cpp           \
../../Archive/LzhHandler.cpp          \
../../Archive/LzmaHandler.cpp          \
../../Archive/MachoHandler.cpp        \
../../Archive/MbrHandler.cpp          \
../../Archive/MslzHandler.cpp         \
../../Archive/MubHandler.cpp          \
../../Archive/NtfsHandler.cpp         \
../../Archive/PeHandler.cpp           \
../../Archive/PpmdHandler.cpp         \
../../Archive/RpmHandler.cpp          \
../../Archive/SplitHandler.cpp        \
../../Archive/SwfHandler.cpp          \
../../Archive/SquashfsHandler.cpp     \
../../Archive/VhdHandler.cpp          \
../../Archive/XarHandler.cpp           \
../../Archive/XzHandler.cpp           \
../../Archive/ZHandler.cpp
LOCAL_SRC_FILES+=../../Archive/Common/CoderMixer2.cpp                       \
../../Archive/Common/CoderMixer2MT.cpp                      \
../../Archive/Common/CrossThreadProgress.cpp               \
../../Archive/Common/DummyOutStream.cpp                     \
../../Archive/Common/FindSignature.cpp                      \
../../Archive/Common/InStreamWithCRC.cpp                    \
../../Archive/Common/ItemNameUtils.cpp                        \
../../Archive/Common/MultiStream.cpp                         \
../../Archive/Common/OutStreamWithCRC.cpp                     \
../../Archive/Common/OutStreamWithSha1.cpp                    \
../../Archive/Common/HandlerOut.cpp                           \
../../Archive/Common/ParseProperties.cpp
LOCAL_SRC_FILES+=../../Archive/7z/7zCompressionMode.cpp        \
../../Archive/7z/7zDecode.cpp                                \
../../Archive/7z/7zEncode.cpp                                 \
../../Archive/7z/7zExtract.cpp                              \
../../Archive/7z/7zFolderInStream.cpp                        \
../../Archive/7z/7zFolderOutStream.cpp                       \
../../Archive/7z/7zHandler.cpp                           \
../../Archive/7z/7zHandlerOut.cpp                           \
../../Archive/7z/7zHeader.cpp                                 \
../../Archive/7z/7zIn.cpp                                    \
../../Archive/7z/7zOut.cpp                                    \
../../Archive/7z/7zProperties.cpp                          \
../../Archive/7z/7zSpecStream.cpp                        \
../../Archive/7z/7zUpdate.cpp                                  \
../../Archive/7z/7zRegister.cpp
LOCAL_SRC_FILES+=../../Archive/Cab/CabBlockInStream.cpp            \
../../Archive/Cab/CabHandler.cpp                        \
../../Archive/Cab/CabHeader.cpp                        \
../../Archive/Cab/CabIn.cpp                            \
../../Archive/Cab/CabRegister.cpp                     \
../../Archive/Chm/ChmHandler.cpp              \
../../Archive/Chm/ChmHeader.cpp                \
../../Archive/Chm/ChmIn.cpp                     \
../../Archive/Chm/ChmRegister.cpp            \
../../Archive/Com/ComHandler.cpp       \
../../Archive/Com/ComIn.cpp             \
../../Archive/Com/ComRegister.cpp

LOCAL_SRC_FILES+=../../Archive/Hfs/HfsHandler.cpp             \
../../Archive/Hfs/HfsIn.cpp                    \
../../Archive/Hfs/HfsRegister.cpp          \
../../Archive/Iso/IsoHandler.cpp             \
../../Archive/Iso/IsoHeader.cpp          \
../../Archive/Iso/IsoIn.cpp                \
../../Archive/Iso/IsoRegister.cpp
LOCAL_SRC_FILES+=../../Archive/Nsis/NsisDecode.cpp      \
../../Archive/Nsis/NsisHandler.cpp               \
../../Archive/Nsis/NsisIn.cpp               \
../../Archive/Nsis/NsisRegister.cpp
LOCAL_SRC_FILES+=../../Archive/Rar/RarHandler.cpp              \
../../Archive/Rar/RarHeader.cpp                  \
../../Archive/Rar/RarIn.cpp                   \
../../Archive/Rar/RarItem.cpp                   \
../../Archive/Rar/RarVolumeInStream.cpp             \
../../Archive/Rar/RarRegister.cpp

LOCAL_SRC_FILES+=../../Archive/Tar/TarHandler.cpp               \
../../Archive/Tar/TarHandlerOut.cpp                 \
../../Archive/Tar/TarHeader.cpp                  \
../../Archive/Tar/TarIn.cpp                   \
../../Archive/Tar/TarOut.cpp                      \
../../Archive/Tar/TarRegister.cpp                 \
../../Archive/Tar/TarUpdate.cpp
			
LOCAL_SRC_FILES+=../../Archive/Udf/UdfHandler.cpp                \
../../Archive/Udf/UdfIn.cpp              \
../../Archive/Udf/UdfRegister.cpp

LOCAL_SRC_FILES+=../../Archive/Wim/WimHandler.cpp                 \
../../Archive/Wim/WimHandlerOut.cpp            \
../../Archive/Wim/WimIn.cpp                         \
../../Archive/Wim/WimRegister.cpp

LOCAL_SRC_FILES+=../../Archive/Zip/ZipAddCommon.cpp          \
../../Archive/Zip/ZipHandler.cpp                 \
../../Archive/Zip/ZipHandlerOut.cpp           \
../../Archive/Zip/ZipHeader.cpp               \
../../Archive/Zip/ZipIn.cpp                        \
../../Archive/Zip/ZipItem.cpp               \
../../Archive/Zip/ZipOut.cpp                   \
../../Archive/Zip/ZipUpdate.cpp                   \
../../Archive/Zip/ZipRegister.cpp
LOCAL_SRC_FILES+=../../Compress/CodecExports.cpp             \
../../Compress/ArjDecoder1.cpp                   \
../../Compress/ArjDecoder2.cpp               \
../../Compress/Bcj2Coder.cpp                  \
../../Compress/Bcj2Register.cpp                      \
../../Compress/BcjCoder.cpp                      \
../../Compress/BcjRegister.cpp               \
../../Compress/BitlDecoder.cpp                 \
../../Compress/BranchCoder.cpp                     \
../../Compress/BranchMisc.cpp                   \
../../Compress/BranchRegister.cpp              \
../../Compress/ByteSwap.cpp                       \
../../Compress/BZip2Crc.cpp                      \
../../Compress/BZip2Decoder.cpp                \
../../Compress/BZip2Encoder.cpp                      \
../../Compress/BZip2Register.cpp                    \
../../Compress/CopyCoder.cpp                   \
../../Compress/CopyRegister.cpp                        \
../../Compress/Deflate64Register.cpp                     \
../../Compress/DeflateDecoder.cpp                   \
../../Compress/DeflateEncoder.cpp                 \
../../Compress/DeflateRegister.cpp                 \
../../Compress/DeltaFilter.cpp                          \
../../Compress/Lzma2Decoder.cpp                        \
../../Compress/Lzma2Encoder.cpp                     \
../../Compress/Lzma2Register.cpp              \
../../Compress/ImplodeDecoder.cpp                        \
../../Compress/ImplodeHuffmanDecoder.cpp                \
../../Compress/LzhDecoder.cpp                        \
../../Compress/LzmaDecoder.cpp                          \
../../Compress/LzmaEncoder.cpp                       \
../../Compress/LzmaRegister.cpp                         \
../../Compress/LzOutWindow.cpp                        \
../../Compress/Lzx86Converter.cpp                     \
../../Compress/LzxDecoder.cpp                          \
../../Compress/PpmdDecoder.cpp                    \
../../Compress/PpmdEncoder.cpp                       \
../../Compress/PpmdRegister.cpp                          \
../../Compress/PpmdZip.cpp                             \
../../Compress/QuantumDecoder.cpp                       \
../../Compress/Rar1Decoder.cpp                                     \
../../Compress/Rar2Decoder.cpp                                   \
../../Compress/Rar3Decoder.cpp                                   \
../../Compress/Rar3Vm.cpp                                        \
../../Compress/RarCodecsRegister.cpp                             \
../../Compress/ShrinkDecoder.cpp                      \
../../Compress/ZlibDecoder.cpp                          \
../../Compress/ZlibEncoder.cpp                           \
../../Compress/ZDecoder.cpp

LOCAL_SRC_FILES+=../../Crypto/7zAes.cpp                  \
../../Crypto/7zAesRegister.cpp                     \
../../Crypto/HmacSha1.cpp                         \
../../Crypto/MyAes.cpp                               \
../../Crypto/Pbkdf2HmacSha1.cpp                    \
../../Crypto/RandGen.cpp                             \
../../Crypto/Sha1.cpp                              \
../../Crypto/WzAes.cpp                                \
../../Crypto/Rar20Crypto.cpp                         \
../../Crypto/RarAes.cpp                            \
../../Crypto/ZipCrypto.cpp                             \
../../Crypto/ZipStrong.cpp
LOCAL_SRC_FILES+=../../../../C/7zCrc.c            \
../../../../C/7zCrcOpt.c

LOCAL_STATIC_LIBRARIES := Threads
include $(BUILD_SHARED_LIBRARY)