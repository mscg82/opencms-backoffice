/*
 * SemaphoreState.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef SEMAPHORESTATE_H_
#define SEMAPHORESTATE_H_

#include <windows.h>

namespace sharedmemory {
namespace semaphore {

	class SemaphoreState {

	private:
		DWORD status;

	public:
		SemaphoreState(DWORD status);
		SemaphoreState(const SemaphoreState& orig);
		virtual ~SemaphoreState();

		SemaphoreState& operator=(const SemaphoreState& orig);

		bool isSignaled() const;
		bool isTimedOut() const;
		bool isErrorState() const;
		bool isAbandoned() const;

		DWORD getStatus() const;

	};

}
}

#endif /* SEMAPHORESTATE_H_ */
