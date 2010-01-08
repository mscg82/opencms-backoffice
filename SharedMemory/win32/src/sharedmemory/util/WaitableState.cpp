/*
 * WaitableState.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/util/WaitableState.h>

namespace sharedmemory {
namespace util {

	WaitableState::WaitableState(DWORD status) {
		this->status = status;
	}

	WaitableState::WaitableState(const WaitableState& orig) {
		this->status = orig.status;
	}

	WaitableState::~WaitableState() {

	}

	WaitableState& WaitableState::operator=(const WaitableState& orig) {
		this->status = orig.status;
		return *this;
	}

	bool WaitableState::isSignaled() const {
		return status == WAIT_OBJECT_0;
	}

	bool WaitableState::isTimedOut() const {
		return status == WAIT_TIMEOUT;
	}

	bool WaitableState::isErrorState() const {
		return status == WAIT_FAILED;
	}

	bool WaitableState::isAbandoned() const {
		return status == WAIT_ABANDONED;
	}

	DWORD WaitableState::getStatus() const {
		return status;
	}

}
}
