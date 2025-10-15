# ZeeArchiver
![](https://github.com/mahmoudgalal/ZeeArchiver/raw/master/app/src/main/res/drawable-hdpi/zeearchiver.png)      
Zee is an efficient and simple to use Archiver and decompressor. it can decompress and compress from-to all the formats supported by the well known 7zip utility. <br/>Copyright © 2025 Mahmoud Galal <br/>For support contact me:mahmoudgalal57@yahoo.com
<br/>
### How to Build:<br/>
First ,you have to compile the native part of the project,[P7Zip](http://p7zip.sourceforge.net/),the repo comes with version 9.20.1 source.
  - Download [Android NDK](https://developer.android.com/ndk/)  ,version <b>r23</b> recommended 
  - From the command line,browse to sub-directory <b>"\p7zip_9.20.1\CPP\7zip\Android\jni"</b> and invoke <b>"ndk-build"</b>
  The first native module <b>"lib7z.so"</b> get compiled for the <b>armeabi-v7a</b> ABI (You can add other ABIs if you want in application.mk file).
  - Copy the compiled "lib7z.so" module to the sub-directory <b>("/Prebuilt/armeabi-v7a/")</b> under the repo .(If you targets other ABI then move to the
  appropriate folder under "/Prebuilt/..".
  - Open the ZeeArchiver project in android studio and start building the app.
<br/>
<i>Note that the app adds a small jni layer(<b>libzeearchiver.so</b>) over lib7z.so module to make use of the 7z library in a proper way for android</i>.
<br/> 

### Migrating to p7zip v16.02:<br/>
<b>P7zip v16.02</b> adds alot of enhancements & fixes to the original port in addition to supporting Rar5 format.<br/> 
To build the app against the newest P7zip version(stripped-down clone of v16.02 is added to this repo) ,
simply Open the ZeeArchiver project in android studio and start building the app, the native plugins <b>lib7z.so & libRar.so</b> 
will be built and copied automatically to "/Prebuilt" sub-directory.

### Features:

- Supports the following formats:
  - <i>Unpacking only:</i> AR, ARJ, CAB, CHM, CPIO, CramFS, DMG, EXT, FAT, GPT, HFS, IHEX, ISO, LZH, LZMA, MBR, MSI, NSIS, NTFS, QCOW2,     RAR, RPM, SquashFS, UDF, UEFI, VDI, VHD, VMDK, WIM, XAR and Z.
  
  - <i>Packing / unpacking:</i>7z, XZ, BZIP2, GZIP, TAR, ZIP and WIM .
- Supports <b>AES-256 encryption</b> in 7z and ZIP formats .
- A built-in file browser.
- Arabic Localization .

 ![](https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png)
<br/>   
https://play.google.com/store/apps/details?id=com.mg.zeearchiver

<br/><b>The App is published at Amazon store:</b><br/>
https://www.amazon.com/dp/B07K5F9DCR/
<br/>  
![](https://github.com/mahmoudgalal/ZeeArchiver/raw/master/device-2018-11-03-145756.png)
![](https://github.com/mahmoudgalal/ZeeArchiver/raw/master/device-2018-11-03-150040.png)


<br/><b>How to Extract:</b><br/>
[![Extract](https://img.youtube.com/vi/J96WcGQQ3uI/hqdefault.jpg)](https://www.youtube.com/watch?v=J96WcGQQ3uI)

<br/><b>How to Compress:</b><br/>
[![Compress](https://img.youtube.com/vi/kWQPEnSiC9k/hqdefault.jpg)](https://www.youtube.com/watch?v=kWQPEnSiC9k)


### License:
[MIT](https://raw.githubusercontent.com/mahmoudgalal/ZeeArchiver/master/LICENSE)
