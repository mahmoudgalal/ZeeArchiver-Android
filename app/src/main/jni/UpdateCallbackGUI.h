// UpdateCallbackGUI.h

#ifndef __UPDATE_CALLBACK_GUI_H
#define __UPDATE_CALLBACK_GUI_H

#include "Update.h"
#include "ArchiveOpenCallback.h"
#include "methodids.h"
//#include "../FileManager/ProgressDialog2.h"

class CUpdateCallbackGUI:
  public IOpenCallbackUI,
  public IUpdateCallbackUI2
{
public:
  bool PasswordIsDefined;
  UString Password;
  bool AskPassword;
  bool PasswordWasAsked;
  UInt64 NumFiles;
  Environment& enviro;

  /*CUpdateCallbackGUI():
      PasswordIsDefined(false),
      PasswordWasAsked(false),
      AskPassword(false)
      {}*/
  CUpdateCallbackGUI(Environment& env):
        PasswordIsDefined(false),
        PasswordWasAsked(false),
        AskPassword(false),
        enviro(env)
        {}
  
  ~CUpdateCallbackGUI();
  void Init();

  INTERFACE_IUpdateCallbackUI2(;)
  INTERFACE_IOpenCallbackUI(;)

  UStringVector FailedFiles;

  //CProgressDialog *ProgressDialog;

  void AddErrorMessage(LPCWSTR message);
  void AddErrorMessage(const wchar_t *name, DWORD systemError);
};

#endif
