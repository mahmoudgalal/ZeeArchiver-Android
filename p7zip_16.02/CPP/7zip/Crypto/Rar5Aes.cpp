// Crypto/Rar5Aes.cpp

#include "StdAfx.h"

#include "../../../C/CpuArch.h"

#ifndef _7ZIP_ST
#include "../../Windows/Synchronization.h"
#endif

#include "Rar5Aes.h"

namespace NCrypto {
namespace NRar5 {

static const unsigned kNumIterationsLog_Max = 24;

static const unsigned kPswCheckCsumSize = 4;
static const unsigned kCheckSize = kPswCheckSize + kPswCheckCsumSize;

CKey::CKey():
    _needCalc(true),
    _numIterationsLog(0)
{
  for (unsigned i = 0; i < sizeof(_salt); i++)
    _salt[i] = 0;
}

CDecoder::CDecoder(): CAesCbcDecoder(kAesKeySize) {}

static unsigned ReadVarInt(const Byte *p, unsigned maxSize, UInt64 *val)
{
  unsigned i;
  *val = 0;

  for (i = 0; i < maxSize;)
  {
    Byte b = p[i];
    if (i < 10)
      *val |= (UInt64)(b & 0x7F) << (7 * i++);
    if ((b & 0x80) == 0)
      return i;
  }
  return 0;
}

HRESULT CDecoder::SetDecoderProps(const Byte *p, unsigned size, bool includeIV, bool isService)
{
  UInt64 Version;
  
  unsigned num = ReadVarInt(p, size, &Version);
  if (num == 0)
    return E_NOTIMPL;
  p += num;
  size -= num;

  if (Version != 0)
    return E_NOTIMPL;
  
  num = ReadVarInt(p, size, &Flags);
  if (num == 0)
    return E_NOTIMPL;
  p += num;
  size -= num;

  bool isCheck = IsThereCheck();
  if (size != 1 + kSaltSize + (includeIV ? AES_BLOCK_SIZE : 0) + (unsigned)(isCheck ? kCheckSize : 0))
    return E_NOTIMPL;

  if (_numIterationsLog != p[0])
  {
    _numIterationsLog = p[0];
    _needCalc = true;
  }

  p++;
    
  if (memcmp(_salt, p, kSaltSize) != 0)
  {
    memcpy(_salt, p, kSaltSize);
    _needCalc = true;
  }
  
  p += kSaltSize;
  
  if (includeIV)
  {
    memcpy(_iv, p, AES_BLOCK_SIZE);
    p += AES_BLOCK_SIZE;
  }
  
  _canCheck = true;
  
  if (isCheck)
  {
    memcpy(_check, p, kPswCheckSize);
    CSha256 sha;
    Byte digest[SHA256_DIGEST_SIZE];
    Sha256_Init(&sha);
    Sha256_Update(&sha, _check, kPswCheckSize);
    Sha256_Final(&sha, digest);
    _canCheck = (memcmp(digest, p + kPswCheckSize, kPswCheckCsumSize) == 0);
    if (_canCheck && isService)
    {
      // There was bug in RAR 5.21- : PswCheck field in service records ("QO") contained zeros.
      // so we disable password checking for such bad records.
      _canCheck = false;
      for (unsigned i = 0; i < kPswCheckSize; i++)
        if (p[i] != 0)
        {
          _canCheck = true;
          break;
        }
    }
  }

  return (_numIterationsLog <= kNumIterationsLog_Max ? S_OK : E_NOTIMPL);
}


void CDecoder::SetPassword(const Byte *data, size_t size)
{
  if (size != _password.Size() || memcmp(data, _password, size) != 0)
  {
    _needCalc = true;
    _password.CopyFrom(data, size);
  }
}


STDMETHODIMP CDecoder::Init()
{
  CalcKey_and_CheckPassword();
  RINOK(SetKey(_key, kAesKeySize));
  RINOK(SetInitVector(_iv, AES_BLOCK_SIZE));
  return CAesCbcCoder::Init();
}


UInt32 CDecoder::Hmac_Convert_Crc32(UInt32 crc) const
{
  NSha256::CHmac ctx;
  ctx.SetKey(_hashKey, NSha256::kDigestSize);
  Byte v[4];
  SetUi32(v, crc);
  ctx.Update(v, 4);
  Byte h[NSha256::kDigestSize];
  ctx.Final(h);
  crc = 0;
  for (unsigned i = 0; i < NSha256::kDigestSize; i++)
    crc ^= (UInt32)h[i] << ((i & 3) * 8);
  return crc;
};


void CDecoder::Hmac_Convert_32Bytes(Byte *data) const
{
  NSha256::CHmac ctx;
  ctx.SetKey(_hashKey, NSha256::kDigestSize);
  ctx.Update(data, NSha256::kDigestSize);
  ctx.Final(data);
};


#ifndef _7ZIP_ST
  static CKey g_Key;
  static NWindows::NSynchronization::CCriticalSection g_GlobalKeyCacheCriticalSection;
  #define MT_LOCK NWindows::NSynchronization::CCriticalSectionLock lock(g_GlobalKeyCacheCriticalSection);
#else
  static CKey g_Key;
  #define MT_LOCK
#endif

bool CDecoder::CalcKey_and_CheckPassword()
{
  if (_needCalc)
  {
    {
      MT_LOCK
      if (!g_Key._needCalc && IsKeyEqualTo(g_Key))
      {
        CopyCalcedKeysFrom(g_Key);
        _needCalc = false;
      }
    }
    
    if (_needCalc)
    {
      Byte pswCheck[SHA256_DIGEST_SIZE];

      {
        // Pbkdf HMAC-SHA-256

        NSha256::CHmac baseCtx;
        baseCtx.SetKey(_password, _password.Size());
        
        NSha256::CHmac ctx = baseCtx;
        ctx.Update(_salt, sizeof(_salt));
        
        Byte u[NSha256::kDigestSize];
        Byte key[NSha256::kDigestSize];
        
        u[0] = 0;
        u[1] = 0;
        u[2] = 0;
        u[3] = 1;
        
        ctx.Update(u, 4);
        ctx.Final(u);
        
        memcpy(key, u, NSha256::kDigestSize);
        
        UInt32 numIterations = ((UInt32)1 << _numIterationsLog) - 1;
        
        for (unsigned i = 0; i < 3; i++)
        {
          UInt32 j = numIterations;
          
          for (; j != 0; j--)
          {
            ctx = baseCtx;
            ctx.Update(u, NSha256::kDigestSize);
            ctx.Final(u);
            for (unsigned s = 0; s < NSha256::kDigestSize; s++)
              key[s] ^= u[s];
          }
          
          // RAR uses additional iterations for additional keys
          memcpy((i == 0 ? _key : (i == 1 ? _hashKey : pswCheck)), key, NSha256::kDigestSize);
          numIterations = 16;
        }
      }

      {
        unsigned i;
       
        for (i = 0; i < kPswCheckSize; i++)
          _check_Calced[i] = pswCheck[i];
      
        for (i = kPswCheckSize; i < SHA256_DIGEST_SIZE; i++)
          _check_Calced[i & (kPswCheckSize - 1)] ^= pswCheck[i];
      }

      _needCalc = false;
      
      {
        MT_LOCK
        g_Key = *this;
      }
    }
  }
  
  if (IsThereCheck() && _canCheck)
    return (memcmp(_check_Calced, _check, kPswCheckSize) == 0);
  return true;
}

}}
