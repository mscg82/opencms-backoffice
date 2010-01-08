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

using namespace std;
using namespace sharedmemory::util;

namespace sharedmemory {

	class SharedMemory {
	protected:
		string sharedMemoryName;
		HANDLE sharedMemoryHandle;
		void*  sharedMemoryData;

		DWORD maxSizeHigh;
		DWORD maxSizeLow;

	public:
		SharedMemory(string sharedMemoryName, DWORD maxSizeHigh, DWORD maxSizeLow);
		virtual ~SharedMemory();

		DWORD getMaxSizeHigh() const;

	    DWORD getMaxSizeLow() const;

	    void* getSharedMemoryData() const;

	    HANDLE getSharedMemoryHandle() const;

	    string getSharedMemoryName() const;

	};

}

#endif /* SHAREDMEMORY_H_ */
