/*
 * Copyright (c) 2018. Created by : Mahmoud Galal.
 * Support: mahmoudgalal57@yahoo.com
 */

package com.aroma.zeearchiver;

public interface UpdateCallback {
		
	void addErrorMessage(String message);
	long startArchive(String name, boolean updating );
	long checkBreak();
	long scanProgress(long  numFolders, long numFiles, String path);
	long setNumFiles(long numFiles);
	long setTotal(long total);
	long setCompleted(long completeValue);
	long setRatioInfo(long inSize, long outSize);
	long getStream(String name, boolean  isAnti );
	long setOperationResult(long  operationResult );
	long openCheckBreak();
	long openSetCompleted( long numFiles, long numBytes );
}
