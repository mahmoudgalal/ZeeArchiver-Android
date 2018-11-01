# ZeeArchiver
![](https://github.com/mahmoudgalal/ZeeArchiver/raw/master/app/src/main/res/drawable-hdpi/zeearchiver.png)      
Zee is an efficient and simple to use Archiver and decompressor. it can decompress and compress from-to all the formats supported by the well known 7zip utility. <br/>Copyright © 2018 Mahmoud Galal <br/>For support contact me:mahmoudgalal57@yahoo.com
<br/>
### How to Build:<br/>
First ,you have to compile the native part of the project,[P7Zip](http://p7zip.sourceforge.net/),the repo comes with version 9.20.1 source.
  - Download [Android NDK](https://developer.android.com/ndk/)  ,version <b>r18b</b> recommended 
  - From the command line,browse to sub-directory <b>"\p7zip_9.20.1\CPP\7zip\Android\jni"</b> and invoke <b>"ndk-build"</b>
  The first native module <b>"lib7z.so"</b> get compiled for the <b>armeabi-v7a</b> ABI (You can add other ABIs if you want in application.mk file).
  - Copy the compiled "lib7z.so" module to the sub-directory <b>("/Prebuilt/armeabi-v7a/")</b> under the repo .(If you targets other ABI then move to the
  appropriate folder under "/Prebuilt/..".
  - Open the ZeeArchiver project in android studio and start building the app.
<br/>
<i>Note that the app adds a small jni layer(<b>libzeearchiver.so</b>) over lib7z.so module to make use of the 7z library in a proper way for android</i>.
<br/>   
<b>Features:</b><br/>

- Supports the following formats:
  - <i>Unpacking only:</i> AR, ARJ, CAB, CHM, CPIO, CramFS, DMG, EXT, FAT, GPT, HFS, IHEX, ISO, LZH, LZMA, MBR, MSI, NSIS, NTFS, QCOW2,     RAR, RPM, SquashFS, UDF, UEFI, VDI, VHD, VMDK, WIM, XAR and Z.
  
  - <i>Packing / unpacking:</i>7z, XZ, BZIP2, GZIP, TAR, ZIP and WIM .
- Supports <b>AES-256 encryption</b> in 7z and ZIP formats .
- A built-in file browser.
- Arabic Localization .

<br/><b>How to Extract:</b><br/>
[![Extract](https://img.youtube.com/vi/J96WcGQQ3uI/hqdefault.jpg)](https://www.youtube.com/watch?v=J96WcGQQ3uI)



