/*
 * Semaphore.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef SEMAPHORE_H_
#define SEMAPHORE_H_

#include <windows.h>
#include <string>
#include <cpp/lang/NullPointerException.h>
#include <sharedmemory/util/Util.h>
#include <sharedmemory/util/WaitableObject.h>
#include <sharedmemory/exception/CreateSemaphoreException.h>
#include <sharedmemory/exception/WaitSemaphoreException.h>
#include <sharedmemory/exception/SignalSemaphoreException.h>

using namespace std;
using namespace cpp::lang;
using namespace sharedmemory::util;
using namespace sharedmemory::exception;

namespace sharedmemory {
namespace semaphore {

	class Semaphore: public WaitableObject {

	protected:
		string semaphoreName;
		LONG initialCount;
		LONG maxCount;

	public:
		Semaphore(string semaphoreName, LONG initialCount, LONG maxCount);
		virtual ~Semaphore();

		virtual string getSemaphoreName() const;
		virtual HANDLE getSemaphoreHandle() const;
		virtual LONG getInitialCount() const;
		virtual LONG getMaxCount() const;

		virtual WaitableState waitFor(DWORD milliseconds);

		LONG signal();
		LONG signal(LONG count);
	};

}
}

#endif /* SEMAPHORE_H_ */
