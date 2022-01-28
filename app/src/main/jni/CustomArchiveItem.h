// CustomArchiveItem.h

#ifndef __CUSTOM_ARCHIVE_ITEM_H
#define __CUSTOM_ARCHIVE_ITEM_H

#include "Common/MyString.h"
#include "C/Types.h"

struct CustomArchiveItem
{
  CSysString itemPath;
  CSysString time;
  UInt64 packSize, unpackSize;
  bool isFolder;
};

#endif
