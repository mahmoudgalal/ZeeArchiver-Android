// GUI/UpdateGUI.h

#ifndef __UPDATE_GUI_H
#define __UPDATE_GUI_H

#include "Update.h"

#include "UpdateCallbackGUI.h"

#include "Windows/Error.h"
#include "Windows/FileDir.h"
#include "Windows/Thread.h"

/*
  callback->FailedFiles contains names of files for that there were problems.
  RESULT can be S_OK, even if there are such warnings!!!
  
  RESULT = E_ABORT - user break.
  RESULT != E_ABORT:
  {
   messageWasDisplayed = true  - message was displayed already.
   messageWasDisplayed = false - there was some internal error, so you must show error message.
  }
*/

namespace NUpdateMode
  {
    enum EEnum
    {
      kAdd,
      kUpdate,
      kFresh,
      kSynchronize
    };
  }
  struct CInfo
  {
    NUpdateMode::EEnum UpdateMode;
    bool SolidIsSpecified;
    bool MultiThreadIsAllowed;
    UInt64 SolidBlockSize;
    UInt32 NumThreads;

    CRecordVector<UInt64> VolumeSizes;

    UInt32 Level;
    UString Method;
    UInt32 Dictionary;
    bool OrderMode;
    UInt32 Order;
    UString Options;

    UString EncryptionMethod;

    bool SFXMode;
    bool OpenShareForWrite;


    UString ArchiveName; // in: Relative for ; out: abs
    UString CurrentDirPrefix;
    bool KeepName;

    bool GetFullPathName(UString &result) const;

    int FormatIndex;

    UString Password;
    bool EncryptHeadersIsAllowed;
    bool EncryptHeaders;

    void Init()
    {
      Level = Dictionary = Order = UInt32(-1);
      OrderMode = false;
      Method.Empty();
      Options.Empty();
      EncryptionMethod.Empty();
    }
    CInfo()
    {
      Init();
    }
  };



HRESULT UpdateGUI(
    CCodecs *codecs,
    const NWildcard::CCensor &censor,
    CUpdateOptions &options,
    bool showDialog,
    bool &messageWasDisplayed,
    CUpdateCallbackGUI *callback,
    CInfo &info,
    HWND hwndParent = NULL);

#endif
