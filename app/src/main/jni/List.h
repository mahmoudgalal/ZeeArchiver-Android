// List.h

#ifndef __LIST_H
#define __LIST_H

#include "Common/Wildcard.h"
#include "LoadCodecs.h"
#include "CustomArchiveItem.h"

typedef CObjectVector<CustomArchiveItem> CustomArchiveItemList ;
HRESULT ListArchives(CCodecs *codecs, const CIntVector &formatIndices,
    bool stdInMode,
    UStringVector &archivePaths, UStringVector &archivePathsFull,
    const NWildcard::CCensorNode &wildcardCensor,
    bool enableHeaders, bool techMode,
    #ifndef _NO_CRYPTO
    bool &passwordEnabled, UString &password,
    #endif
    UInt64 &errors,
    CustomArchiveItemList& listData);

#endif

