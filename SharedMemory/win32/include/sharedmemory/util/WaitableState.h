/*
 *  WaitableState.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef WAITABLESTATE_H_
#define WAITABLESTATE_H_

#include <windows.h>

namespace sharedmemory {
namespace util {

	class WaitableState {

	private:
		DWORD status;

	public:
		WaitableState(DWORD status);
		WaitableState(const WaitableState& orig);
		virtual ~WaitableState();

		WaitableState& operator=(const WaitableState& orig);

		bool isSignaled() const;
		bool isTimedOut() const;
		bool isErrorState() const;
		bool isAbandoned() const;

		DWORD getStatus() const;

	};

}
}

#endif /* WaitableState_H_ */
