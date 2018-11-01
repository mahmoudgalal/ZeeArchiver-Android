// UpdateGUI.cpp

#include "StdAfx.h"

#include "UpdateGUI.h"

#include "Common/IntToString.h"
#include "Common/StringConvert.h"
#include "Common/StringToInt.h"



//#include "../Common/WorkDir.h"

//#include "../Explorer/MyMessages.h"

//#include "../FileManager/LangUtils.h"
#include "ProgramLocation.h"
#include "StringUtils.h"
//#include "../FileManager/resourceGui.h"

//#include "CompressDialog.h"
#include "UpdateGUI.h"

//#include "resource2.h"

using namespace NWindows;
using namespace NFile;



static const wchar_t *kDefaultSfxModule = L"7z.sfx";
static const wchar_t *kSFXExtension = L"exe";


bool CInfo::GetFullPathName(UString &result) const
{
        #ifndef UNDER_CE
	     NDirectory::MySetCurrentDirectory(CurrentDirPrefix);
        #endif
        return NDirectory::MyGetFullPathName(ArchiveName, result);
}

extern void AddMessageToString(UString &dest, const UString &src);

UString HResultToMessage(HRESULT errorCode);


class CThreadUpdating//: public CProgressThreadVirt
{
  HRESULT ProcessVirt();
public:
  CCodecs *codecs;
  CUpdateCallbackGUI *UpdateCallbackGUI;
  const NWildcard::CCensor *WildcardCensor;
  CUpdateOptions *Options;
  UString Title;
  UString OkMessage;
  UString ErrorMessage;
  UString ErrorPath1;
  UString ErrorPath2;
  HRESULT Result;
  bool ThreadFinishedOK;
  void Process();
  HRESULT Create();
};
 
HRESULT CThreadUpdating::ProcessVirt()
{
  CUpdateErrorInfo ei;
  HRESULT res = UpdateArchive(codecs, *WildcardCensor, *Options,
     ei, UpdateCallbackGUI, UpdateCallbackGUI);
  ErrorMessage = ei.Message;
  ErrorPath1 = ei.FileName;
  ErrorPath2 = ei.FileName2;
  if (ei.SystemError != S_OK && ei.SystemError != E_FAIL && ei.SystemError != E_ABORT)
    return ei.SystemError;
  return res;
}
HRESULT CThreadUpdating::Create()
{
	try
		{
		  Process();
		  ThreadFinishedOK = true;
		}
	catch (...) { Result = E_FAIL; }
	   // return 0;
	return S_OK;
}
void CThreadUpdating::Process()
{
	//CProgressCloser closer(ProgressDialog);
	  UString m;
	  try { Result = ProcessVirt(); }
	  catch(const wchar_t *s) { m = s; }
	  catch(const UString &s) { m = s; }
	  catch(const char *s) { m = GetUnicodeString(s); }
	  catch(...) { m = L"Error"; }
	  if (Result != E_ABORT)
	  {
	    if (m.IsEmpty() && Result != S_OK)
	    {
	      m = HResultToMessage(Result);
	      LOGE("%s",(const char*)GetOemString(L"just error "+m));
	      if(Result==E_FAIL)
	    	  LOGE("Result==E_FAIL");
	    }

	  }
	  AddMessageToString(m, ErrorMessage);
	  AddMessageToString(m, ErrorPath1);
	  AddMessageToString(m, ErrorPath2);

	  LOGE("%s",(const char*)GetOemString(ErrorMessage));
	  LOGE("%s",(const char*)GetOemString(ErrorPath1));
	  LOGE("%s",(const char*)GetOemString(ErrorPath2));

	  if (m.IsEmpty())
	  {
	    if (!OkMessage.IsEmpty())
	    {
	    	LOGI("%s",(const char*)GetOemString(OkMessage));
	      //ProgressDialog.Sync.SetOkMessageTitle(OkMessageTitle);
	      //ProgressDialog.Sync.SetOkMessage(OkMessage);
	    }
	  }
	  else
	  {
		  LOGE("%s",(const char*)GetOemString(m));
	   // ProgressDialog.Sync.SetErrorMessage(m);
	    if (Result == S_OK)
	      Result = E_FAIL;
	    //Added by me
	    if(UpdateCallbackGUI)
	    	UpdateCallbackGUI->AddErrorMessage(m);
	  }
}

static void AddProp(CObjectVector<CProperty> &properties, const UString &name, const UString &value)
{
  CProperty prop;
  prop.Name = name;
  prop.Value = value;
  properties.Add(prop);
}

static void AddProp(CObjectVector<CProperty> &properties, const UString &name, UInt32 value)
{
  wchar_t tmp[32];
  ConvertUInt64ToString(value, tmp);
  AddProp(properties, name, tmp);
}

static void AddProp(CObjectVector<CProperty> &properties, const UString &name, bool value)
{
  AddProp(properties, name, value ? UString(L"on"): UString(L"off"));
}

static bool IsThereMethodOverride(bool is7z, const UString &propertiesString)
{
  UStringVector strings;
  SplitString(propertiesString, strings);
  for (int i = 0; i < strings.Size(); i++)
  {
    const UString &s = strings[i];
    if (is7z)
    {
      const wchar_t *end;
      UInt64 n = ConvertStringToUInt64(s, &end);
      if (n == 0 && *end == L'=')
        return true;
    }
    else
    {
      if (s.Length() > 0)
        if (s[0] == L'm' && s[1] == L'=')
          return true;
    }
  }
  return false;
}

static void ParseAndAddPropertires(CObjectVector<CProperty> &properties,
    const UString &propertiesString)
{
  UStringVector strings;
  SplitString(propertiesString, strings);
  for (int i = 0; i < strings.Size(); i++)
  {
    const UString &s = strings[i];
    CProperty property;
    int index = s.Find(L'=');
    if (index < 0)
      property.Name = s;
    else
    {
      property.Name = s.Left(index);
      property.Value = s.Mid(index + 1);
    }
    properties.Add(property);
  }
}

static UString GetNumInBytesString(UInt64 v)
{
  wchar_t s[32];
  ConvertUInt64ToString(v, s);
  size_t len = wcslen(s);
  s[len++] = L'B';
  s[len] = L'\0';
  return s;
}

static void SetOutProperties(
    CObjectVector<CProperty> &properties,
    bool is7z,
    UInt32 level,
    bool setMethod,
    const UString &method,
    UInt32 dictionary,
    bool orderMode,
    UInt32 order,
    bool solidIsSpecified, UInt64 solidBlockSize,
    bool multiThreadIsAllowed, UInt32 numThreads,
    const UString &encryptionMethod,
    bool encryptHeadersIsAllowed, bool encryptHeaders,
    bool /* sfxMode */)
{
  if (level != (UInt32)(Int32)-1)
    AddProp(properties, L"x", (UInt32)level);
  if (setMethod)
  {
    if (!method.IsEmpty())
      AddProp(properties, is7z ? L"0": L"m", method);
    if (dictionary != (UInt32)(Int32)-1)
    {
      UString name;
      if (is7z)
        name = L"0";
      if (orderMode)
        name += L"mem";
      else
        name += L"d";
      AddProp(properties, name, GetNumInBytesString(dictionary));
    }
    if (order != (UInt32)(Int32)-1)
    {
      UString name;
      if (is7z)
        name = L"0";
      if (orderMode)
        name += L"o";
      else
        name += L"fb";
      AddProp(properties, name, (UInt32)order);
    }
  }
    
  if (!encryptionMethod.IsEmpty())
    AddProp(properties, L"em", encryptionMethod);

  if (encryptHeadersIsAllowed)
    AddProp(properties, L"he", encryptHeaders);
  if (solidIsSpecified)
    AddProp(properties, L"s", GetNumInBytesString(solidBlockSize));
  if (multiThreadIsAllowed)
    AddProp(properties, L"mt", numThreads);
}

static HRESULT ShowDialog(
    CCodecs *codecs,
    const NWildcard::CCensor &censor,
    CUpdateOptions &options, CUpdateCallbackGUI *callback,CInfo &info, HWND hwndParent)
{
  if (options.Commands.Size() != 1)
    throw "It must be one command";
  UString currentDirPrefix;
  #ifndef UNDER_CE
  {
    if (!NDirectory::MyGetCurrentDirectory(currentDirPrefix))
      return E_FAIL;

    NName::NormalizeDirPathPrefix(currentDirPrefix);
  }
  #endif

  bool oneFile = false;
  NFind::CFileInfoW fileInfo;
  UString name;
  if (censor.Pairs.Size() > 0)
  {
    const NWildcard::CPair &pair = censor.Pairs[0];
    if (pair.Head.IncludeItems.Size() > 0)
    {
      const NWildcard::CItem &item = pair.Head.IncludeItems[0];
      if (item.ForFile)
      {
        name = pair.Prefix;
        for (int i = 0; i < item.PathParts.Size(); i++)
        {
          if (i > 0)
            name += WCHAR_PATH_SEPARATOR;
          name += item.PathParts[i];
        }
        if (fileInfo.Find(name))
        {
          if (censor.Pairs.Size() == 1 && pair.Head.IncludeItems.Size() == 1)
            oneFile = !fileInfo.IsDir();
        }
      }
    }
  }
    
  CInfo di;
//  CCompressDialog dialog;
//  NCompressDialog::CInfo &di = dialog.Info;
//  dialog.ArcFormats = &codecs->Formats;
//  for (int i = 0; i < codecs->Formats.Size(); i++)
//  {
//    const CArcInfoEx &ai = codecs->Formats[i];
//    if (ai.Name.CompareNoCase(L"swfc") == 0)
//      if (!oneFile || name.Right(4).CompareNoCase(L".swf") != 0)
//        continue;
//    if (ai.UpdateEnabled && (oneFile || !ai.KeepName))
//      dialog.ArcIndices.Add(i);
//  }
//  if (dialog.ArcIndices.Size() == 0)
//  {
//    ShowErrorMessage(L"No Update Engines");
//    return E_FAIL;
//  }

  //// di.ArchiveName = options.ArchivePath.GetFinalPath();
  di.ArchiveName = options.ArchivePath.GetPathWithoutExt();
//  dialog.OriginalFileName = options.ArchivePath.Prefix + fileInfo.Name;
//
  di.CurrentDirPrefix = currentDirPrefix;
  di.SFXMode = options.SfxMode;
  di.OpenShareForWrite = options.OpenShareForWrite;
//
  if (callback->PasswordIsDefined)
    di.Password = callback->Password;
//
  di.KeepName = !oneFile;
//
//  if (dialog.Create(hwndParent) != IDOK)
//    return E_ABORT;
//
  options.VolumesSizes = di.VolumeSizes;
  /*
  if (di.VolumeSizeIsDefined)
  {
    MyMessageBox(L"Splitting to volumes is not supported");
    return E_FAIL;
  }
  */
  di.Password = info.Password;
  di.FormatIndex = info.FormatIndex;
  di.ArchiveName =/* L"/mnt/sdcard/devdownloads/test3/"+*/info.ArchiveName;
  di.UpdateMode = NUpdateMode::kAdd;
  NUpdateArchive::CActionSet &actionSet = options.Commands.Front().ActionSet;
  
  switch(di.UpdateMode)
  {
    case /*NCompressDialog::*/NUpdateMode::kAdd:
      actionSet = NUpdateArchive::kAddActionSet;
      break;
    case /*NCompressDialog::*/NUpdateMode::kUpdate:
      actionSet = NUpdateArchive::kUpdateActionSet;
      break;
    case /*NCompressDialog::*/NUpdateMode::kFresh:
      actionSet = NUpdateArchive::kFreshActionSet;
      break;
    case /*NCompressDialog::*/NUpdateMode::kSynchronize:
      actionSet = NUpdateArchive::kSynchronizeActionSet;
      break;
    default:
      throw 1091756;
  }
  const CArcInfoEx &archiverInfo = codecs->Formats[di.FormatIndex];
  callback->PasswordIsDefined = (!di.Password.IsEmpty());
  if (callback->PasswordIsDefined)
    callback->Password = di.Password;

  options.MethodMode.Properties.Clear();

  bool is7z = archiverInfo.Name.CompareNoCase(L"7z") == 0;
  bool methodOverride = IsThereMethodOverride(is7z, di.Options);

  di.NumThreads=0;//Overridden ...myimplementation
  di.OpenShareForWrite=false;//Overridden ...myimplementation
  di.SFXMode=false;//Overridden ...myimplementation

  di.Level = info.Level;
  di.Method = info.Method;
  di.Dictionary = info.Dictionary;
  di.OrderMode = info.OrderMode;
  di.Order = info.Order;
  di.SolidIsSpecified = info.SolidIsSpecified;
  di.SolidBlockSize = info.SolidBlockSize;
  di.MultiThreadIsAllowed = info.MultiThreadIsAllowed;
  //di.NumThreads = info.NumThreads;
  di.EncryptionMethod = info.EncryptionMethod;
  di.EncryptHeadersIsAllowed = info.EncryptHeadersIsAllowed;
  di.EncryptHeaders = info.EncryptHeaders;

  SetOutProperties(
      options.MethodMode.Properties,
      is7z,
      di.Level,
      !methodOverride,
      di.Method,
      di.Dictionary,
      di.OrderMode, di.Order,
      di.SolidIsSpecified, di.SolidBlockSize,
      di.MultiThreadIsAllowed, di.NumThreads,
      di.EncryptionMethod,
      di.EncryptHeadersIsAllowed, di.EncryptHeaders,
      di.SFXMode);

  LOGI("Compression parameters:ArcName:%s,FormatIndex:%d,Level:%u,Method:%s,Dictionary:%u"
		  ",OrderMode:%d,order:%u,SolidIsSpecified:%d,SolidBlockSize:%g,"
		  "MultiThreadIsAllowed:%d,NumThreads:%u,EncryptionMethod:%s,"
		  "EncryptHeadersIsAllowed:%d,EncryptHeaders:%d,SFXMode:%d",
		  (LPCSTR)GetOemString(di.ArchiveName),di.FormatIndex,di.Level,(LPCSTR)GetOemString(di.Method)
		  ,di.Dictionary,di.OrderMode,di.Order,di.SolidIsSpecified,(double)di.SolidBlockSize,
		  di.MultiThreadIsAllowed,di.NumThreads,(LPCSTR)GetOemString(di.EncryptionMethod),
		  di.EncryptHeadersIsAllowed,di.EncryptHeaders,di.SFXMode);
//
  options.OpenShareForWrite = di.OpenShareForWrite;
  ParseAndAddPropertires(options.MethodMode.Properties, di.Options);
//
  if (di.SFXMode)
    options.SfxMode = true;
  options.MethodMode.FormatIndex = di.FormatIndex;
//
  options.ArchivePath.VolExtension = archiverInfo.GetMainExt();
  if (di.SFXMode)
    options.ArchivePath.BaseExtension = kSFXExtension;
  else
    options.ArchivePath.BaseExtension = options.ArchivePath.VolExtension;
  options.ArchivePath.ParseFromPath(di.ArchiveName);
//
//  NWorkDir::CInfo workDirInfo;
//  workDirInfo.Load();
  options.WorkingDir.Empty();
  options.WorkingDir =ExtractDirPrefixFromPath(di.ArchiveName);// L"/mnt/sdcard/devdownloads/test3/";
  //
//  if (workDirInfo.Mode != NWorkDir::NMode::kCurrent)
//  {
//    UString fullPath;
//    NDirectory::MyGetFullPathName(di.ArchiveName, fullPath);
//    options.WorkingDir = GetWorkDir(workDirInfo, fullPath);
 //   NDirectory::CreateComplexDirectory(options.WorkingDir);
//  }
  return S_OK;
}

HRESULT UpdateGUI(
    CCodecs *codecs,
    const NWildcard::CCensor &censor,
    CUpdateOptions &options,
    bool showDialog,
    bool &messageWasDisplayed,
    CUpdateCallbackGUI *callback,
    CInfo &info,
    HWND hwndParent)
{
  LOGI("UpdateGUI....");
  LOGI("%s",(const char*)GetOemString(info.ArchiveName));
  messageWasDisplayed = false;
  if (showDialog)
  {
    RINOK(ShowDialog(codecs, censor, options, callback,info, hwndParent));
  }
  LOGI("After ShowDialog....");
  if (options.SfxMode && options.SfxModule.IsEmpty())
  {
    UString folder;
    if (!GetProgramFolderPath(folder))
      folder.Empty();
    options.SfxModule = folder + kDefaultSfxModule;
  }

  CThreadUpdating tu;

  tu.codecs = codecs;

  tu.UpdateCallbackGUI = callback;
  //tu.UpdateCallbackGUI->ProgressDialog = &tu.ProgressDialog;
  tu.UpdateCallbackGUI->Init();

 // UString title = LangString(IDS_PROGRESS_COMPRESSING, 0x02000DC0);

  /*
  if (hwndParent != 0)
  {
    tu.ProgressDialog.MainWindow = hwndParent;
    // tu.ProgressDialog.MainTitle = fileName;
    tu.ProgressDialog.MainAddTitle = title + L" ";
  }
  */

  tu.WildcardCensor = &censor;
  tu.Options = &options;
  //tu.ProgressDialog.IconID = IDI_ICON;

  //RINOK(tu.Create(title, hwndParent));
  RINOK(tu.Create());

  messageWasDisplayed = tu.ThreadFinishedOK ;
  //& tu.ProgressDialog.MessagesDisplayed;
  return tu.Result;
}
