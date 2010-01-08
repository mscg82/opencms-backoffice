/*
 * WaitableObject.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/util/WaitableObject.h>

namespace sharedmemory {
namespace util {

	WaitableObject::WaitableObject(HANDLE waitableHandle) {
		setWaitableHandle(waitableHandle);
	}

	WaitableObject::~WaitableObject() {
		if(waitableHandle != NULL) {
			if(CloseHandle(waitableHandle)) {
				waitableHandle = NULL;
			}
		}
	}

	HANDLE WaitableObject::getWaitableHandle() const {
		return waitableHandle;
	}

	void WaitableObject::setWaitableHandle(HANDLE waitableHandle) {
		this->waitableHandle = waitableHandle;
	}

	WaitableState WaitableObject::wait() {
		return this->waitFor(INFINITE);
	}

	WaitableState WaitableObject::waitFor(DWORD milliseconds) {
		if(waitableHandle == NULL) {
			throw NullPointerException();
		}
		WaitableState ret(WaitForSingleObject(waitableHandle, milliseconds));
		return ret;
	}

}
}
