/*
 * SharedMemory.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/SharedMemory.h>

namespace sharedmemory {

	SharedMemory::SharedMemory(string sharedMemoryName, DWORD maxSizeHigh, DWORD maxSizeLow,
			bool autoOpen, HANDLE fileHandle, DWORD accessMode) :
		sharedMemoryName(sharedMemoryName)
	{
		this->maxSizeHigh = maxSizeHigh;
		this->maxSizeLow = maxSizeLow;
		this->accessMode = accessMode;

		this->sharedMemoryHandle = NULL;
		this->sharedMemoryData = NULL;
		this->fileHandle = fileHandle;

		inited = false;

		if(autoOpen)
			open();
	}

	SharedMemory::~SharedMemory() {
		close();
	}

	DWORD SharedMemory::getMaxSizeHigh() const {
		return maxSizeHigh;
	}

	DWORD SharedMemory::getMaxSizeLow() const {
		return maxSizeLow;
	}

	char* SharedMemory::getSharedMemoryData() const {
		return (char*)sharedMemoryData;
	}

	HANDLE SharedMemory::getSharedMemoryHandle() const {
		return sharedMemoryHandle;
	}

	string SharedMemory::getSharedMemoryName() const {
		return sharedMemoryName;
	}

	bool SharedMemory::isInited() const {
		return inited;
	}

	DWORD SharedMemory::getAccessMode() const {
		return accessMode;
	}

	void SharedMemory::open() {
		if(!inited) {
			sharedMemoryHandle =
				CreateFileMapping(fileHandle, NULL, accessMode, maxSizeHigh, maxSizeLow, sharedMemoryName.c_str());
			if(sharedMemoryHandle == NULL) {
				throw CreateFileMappingException(Util::getLastErrorMessage());
			}

			DWORD mapAccess = (accessMode == PAGE_READONLY ? FILE_MAP_READ : FILE_MAP_ALL_ACCESS);
			sharedMemoryData =
				MapViewOfFile(sharedMemoryHandle, mapAccess, 0, 0, maxSizeLow);

			if(sharedMemoryData == NULL) {
				string message = Util::getLastErrorMessage();
				CloseHandle(this->sharedMemoryHandle);
				this->sharedMemoryHandle = NULL;

				throw MapViewOfFileException(message);
			}

			inited = true;
		}
	}

	void SharedMemory::close() {
		if(inited) {
			bool unmapError = true;
			bool closeHandleError = true;

			if(this->sharedMemoryData != NULL) {
				if(UnmapViewOfFile(this->sharedMemoryData)) {
					this->sharedMemoryData = NULL;
					unmapError = false;
				}
			}
			else {
				unmapError = false;
			}

			if(this->sharedMemoryHandle != NULL) {
				if(CloseHandle(this->sharedMemoryHandle)) {
					this->sharedMemoryHandle = NULL;
					closeHandleError = false;
				}
			}
			else {
				closeHandleError = false;
			}

			if(unmapError || closeHandleError) {
				throw UnMapException(Util::getLastErrorMessage());
			}

			inited = false;
		}
	}

}
