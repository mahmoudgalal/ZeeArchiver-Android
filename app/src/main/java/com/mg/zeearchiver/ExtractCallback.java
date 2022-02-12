/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.mg.zeearchiver;

public interface ExtractCallback {

	//Android GUI passward handlers;
	void guiSetPassword(String pass);
	String guiGetPassword();
	boolean guiIsPasswordSet();
	//public ExtractCallback() ;
	void beforeOpen(String name);
	void extractResult(long result);
	void openResult(String name, long result, boolean encrypted);
	long thereAreNoFiles();
	long setPassword(String password);
	
	// IFolderOperationsExtractCallback
	long askWrite(
		                 String srcPath,
		                 int srcIsFolder,
		                 long  srcTime,
		                 long srcSize,
		                 String destPathRequest,
		                 String destPathResult,
		                 int writeAnswer);
	long setCurrentFilePath(String filePath,long numFilesCur);
	long showMessage(String message);
	long setNumFiles(long numFiles);
	//ICompressProgressInfo
	long setRatioInfo(long inSize, long outSize);
	
	//IFolderArchiveExtractCallback
	long askOverwrite(
		      String existName, long existTime, long existSize,
		      String newName, long newTime, long newSize,
		      int answer);
	long prepareOperation(String name, boolean isFolder, int askExtractMode
			, long position);
	long messageError(String message);
	long setOperationResult(int operationResult,long numFilesCur, boolean encrypted);

	// ICryptoGetTextPassword	 
	String cryptoGetTextPassword(String password);
	//IProgress
	long setTotal(long total);
	long setCompleted(long value);
	
	//IOpenCallbackUI

	/**
	 * Called to decide proceeding or cancelling the extraction
	 * @return S_OK(0) to proceed with extraction,any other value to cancel
	 */
	long open_CheckBreak();
	long open_SetTotal(long numFiles /* numFiles */, long numBytes/* numBytes */);
	long open_SetCompleted(long numFiles /* numFiles */, long numBytes/* numBytes */);
	long open_CryptoGetTextPassword(String password);
	long open_GetPasswordIfAny(String password);
	boolean open_WasPasswordAsked();
	void open_ClearPasswordWasAskedFlag();

	void addErrorMessage(String message);

}
