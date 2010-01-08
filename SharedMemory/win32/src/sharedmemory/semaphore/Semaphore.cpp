/*
 * Semaphore.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/semaphore/Semaphore.h>

namespace sharedmemory {
namespace semaphore {

	Semaphore::Semaphore(string semaphoreName, LONG initialCount, LONG maxCount) :
		WaitableObject(), semaphoreName(semaphoreName)
	{
		this->initialCount = initialCount;
		this->maxCount = maxCount;

		HANDLE semaphoreHandle = CreateSemaphore(NULL, this->initialCount, this->maxCount, this->semaphoreName.c_str());
		if(semaphoreHandle == NULL) {
			throw CreateSemaphoreException(Util::getLastErrorMessage());
		}
		setWaitableHandle(semaphoreHandle);
	}

	Semaphore::~Semaphore() {

	}

	string Semaphore::getSemaphoreName() const {
		return semaphoreName;
	}

	HANDLE Semaphore::getSemaphoreHandle() const {
		return WaitableObject::getWaitableHandle();
	}

	LONG Semaphore::getInitialCount() const {
		return initialCount;
	}

	LONG Semaphore::getMaxCount() const {
		return maxCount;
	}

	WaitableState Semaphore::waitFor(DWORD milliseconds) {
		WaitableState ret = WaitableObject::waitFor(milliseconds) ;
		if(ret.isErrorState()) {
			throw WaitSemaphoreException(Util::getLastErrorMessage());
		}
		return ret;
	}

	LONG Semaphore::signal() {
		return signal(1);
	}

	LONG Semaphore::signal(LONG count) {
		if(waitableHandle == NULL) {
			throw NullPointerException();
		}
		LONG old = 0;
		if(!ReleaseSemaphore(waitableHandle, count, &old)) {
			throw SignalSemaphoreException(Util::getLastErrorMessage());
		}
		return old;
	}

}
}
