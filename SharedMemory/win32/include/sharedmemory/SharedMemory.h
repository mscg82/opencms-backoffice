/*
 * SharedMemory.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef SHAREDMEMORY_H_
#define SHAREDMEMORY_H_

#include <windows.h>
#include <string>
#include <sharedmemory/util/Util.h>
#include <sharedmemory/exception/CreateFileMappingException.h>
#include <sharedmemory/exception/MapViewOfFileException.h>
#include <sharedmemory/exception/UnMapException.h>

using namespace std;
using namespace sharedmemory::util;
using namespace sharedmemory::exception;

namespace sharedmemory {

	class SharedMemory {
	protected:
		string sharedMemoryName;
		HANDLE fileHandle;
		HANDLE sharedMemoryHandle;
		void*  sharedMemoryData;

		DWORD maxSizeHigh;
		DWORD maxSizeLow;
		DWORD accessMode;

		bool inited;

	public:
		SharedMemory(string sharedMemoryName, DWORD maxSizeHigh, DWORD maxSizeLow,
				DWORD accessMode = PAGE_READONLY, bool autoOpen = true,
				HANDLE fileHandle = INVALID_HANDLE_VALUE);

		virtual ~SharedMemory();

		virtual DWORD getMaxSizeHigh() const;
		virtual DWORD getMaxSizeLow() const;
		virtual BYTE* getSharedMemoryData() const;
		virtual HANDLE getSharedMemoryHandle() const;
		virtual string getSharedMemoryName() const;
		virtual bool isInited() const;
		virtual DWORD getAccessMode() const;

		virtual void open();
		virtual void close();

	};

}

#endif /* SHAREDMEMORY_H_ */
