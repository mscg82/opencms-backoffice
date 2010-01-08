/*
 * WaitableObject.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef WAITABLEOBJECT_H_
#define WAITABLEOBJECT_H_

#include <windows.h>
#include <sharedmemory/util/WaitableState.h>
#include <cpp/lang/NullPointerException.h>

using namespace cpp::lang;

namespace sharedmemory {
namespace util {

	class WaitableObject {

	protected:
		HANDLE waitableHandle;

	public:
		WaitableObject(HANDLE waitableHandle = NULL);
		virtual ~WaitableObject();

		HANDLE getWaitableHandle() const;

		virtual WaitableState wait();
		virtual WaitableState waitFor(DWORD milliseconds);

	protected:
		void setWaitableHandle(HANDLE waitableHandle);
	};

}
}

#endif /* WAITABLEOBJECT_H_ */
