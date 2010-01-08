/*
 * SharedMemory.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/SharedMemory.h>

namespace sharedmemory {

	SharedMemory::SharedMemory(string sharedMemoryName, DWORD maxSizeHigh, DWORD maxSizeLow) :
		sharedMemoryName(sharedMemoryName)
	{
		this->maxSizeHigh = maxSizeHigh;
		this->maxSizeLow = maxSizeLow;

		this->sharedMemoryHandle = NULL;
		this->sharedMemoryData = NULL;

	}

	SharedMemory::~SharedMemory() {
		if(this->sharedMemoryData != NULL) {
			if(UnmapViewOfFile(this->sharedMemoryData)) {
				this->sharedMemoryData = NULL;
			}
		}

		if(this->sharedMemoryHandle != NULL) {
			if(CloseHandle(this->sharedMemoryHandle)) {
				this->sharedMemoryHandle = NULL;
			}
		}
	}

	DWORD SharedMemory::getMaxSizeHigh() const {
		return maxSizeHigh;
	}

	DWORD SharedMemory::getMaxSizeLow() const {
		return maxSizeLow;
	}

	void* SharedMemory::getSharedMemoryData() const {
		return sharedMemoryData;
	}

	HANDLE SharedMemory::getSharedMemoryHandle() const {
		return sharedMemoryHandle;
	}

	string SharedMemory::getSharedMemoryName() const {
		return sharedMemoryName;
	}

}
